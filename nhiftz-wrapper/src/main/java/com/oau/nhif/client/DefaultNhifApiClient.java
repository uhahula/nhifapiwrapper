package com.oau.nhif.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oau.nhif.client.model.*;
import com.oau.nhif.exception.NhifApiException;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.CompletionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.SneakyThrows;

/**
 * Default implementation of the NHIF API client.
 */
public class DefaultNhifApiClient implements NhifApiClient {
    private static final String TOKEN_ENDPOINT = "/authserver/connect/token";
    private static final String TOKEN_SCOPE = "OnlineServices";
    private static final Logger logger = LoggerFactory.getLogger(DefaultNhifApiClient.class);
    
    private final NhifApiConfig config;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final ScheduledExecutorService scheduler;
    private final AtomicReference<TokenInfo> currentToken = new AtomicReference<>();
    private final TokenPersistence tokenPersistence;
    private ScheduledFuture<?> tokenRefreshFuture;
    private boolean closed = false;
    private final Object tokenLock = new Object();

    public DefaultNhifApiClient(NhifApiConfig config) throws NhifApiException {
        this.config = config;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(config.getConnectionTimeout())
                .build();
        
        // Configure ObjectMapper with appropriate settings
        this.objectMapper = new ObjectMapper()
                .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
                .setPropertyNamingStrategy(com.fasterxml.jackson.databind.PropertyNamingStrategies.UPPER_CAMEL_CASE);
                
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.tokenPersistence = new TokenPersistence();
        
        // Try to load token from disk
        TokenInfo savedToken = tokenPersistence.loadToken();
        if (savedToken != null) {
            logger.info("Using saved token that expires at: {}", savedToken.getExpiresAt());
            currentToken.set(savedToken);
            scheduleTokenRefresh(savedToken.getExpiresAt());
        } else {
            // Initial token fetch if no valid token found
            refreshToken();
        }
    }

    // Implementation of interface methods
    
    @Override
    public CompletableFuture<CardDetails> getCardDetails(String cardNumber) throws NhifApiException {
        // Using the exact endpoint from the working curl command
        return get("/api/Verification/GetCardDetails?cardNo=" + cardNumber, CardDetails.class);
    }

    @Override
    public CompletableFuture<AuthorizationDetails> getAuthorizationDetails(String authorizationNumber) throws NhifApiException {
        return get("/api/Verification/GetAuthorizationDetails?authorizationNo=" + authorizationNumber, AuthorizationDetails.class);
    }

    @Override
    public CompletableFuture<AuthorizationResponse> authorizeCard(AuthorizationRequest request) throws NhifApiException {
        return post("/api/Verification/AuthorizeCard", request, AuthorizationResponse.class);
    }

    @Override
    public CompletableFuture<CardVerification> verifyCard(String cardNumber) throws NhifApiException {
        // Use visitTypeID = 1 for "Normal Visit" (from earlier VisitTypes response)
        VerifyCardRequest request = new VerifyCardRequest(cardNumber, 1);
        return post("/api/Verification/VerifyCard", request, CardVerification.class);
    }

    @Override
    public CompletableFuture<PatientDetails> getPatientDetails(String cardNumber) throws NhifApiException {
        return get("/api/Verification/GetPatientDetails?CardNo=" + cardNumber, PatientDetails.class);
    }

    @Override
    public CompletableFuture<List<PatientVisit>> getPreviousPatientVisits(String cardNumber) throws NhifApiException {
        // API returns single object, not array - wrap in list
        return get("/api/History/GetPreviousPatientVisists?cardNo=" + cardNumber, PatientVisit.class)
                .thenApply(visit -> visit != null ? List.of(visit) : List.of());
    }

    @Override
    public CompletableFuture<MedicalHistory> getMedicalHistory(String cardNumber) throws NhifApiException {
        // Add daysPast parameter (default 60 from swagger)
        return get("/api/History/GetMedicalHistory?cardNo=" + cardNumber + "&daysPast=60", MedicalHistory.class);
    }

    @Override
    public CompletableFuture<TestResponse> testClaim() throws NhifApiException {
        // This endpoint doesn't exist in swagger - placeholder implementation
        return CompletableFuture.completedFuture(new TestResponse("Test endpoint not available", false));
    }

    @Override
    public CompletableFuture<ClaimSubmissionResponse> submitFolio(FolioSubmission request) throws NhifApiException {
        // TODO: Find correct endpoint from swagger or documentation
        throw new NhifApiException("submitFolio endpoint not yet implemented - endpoint not found in swagger", 501, "Not Implemented");
    }

    @Override
    public CompletableFuture<SubmittedClaim> getSubmittedClaim(String claimId) throws NhifApiException {
        // TODO: Find correct endpoint from swagger or documentation
        throw new NhifApiException("getSubmittedClaim endpoint not yet implemented - endpoint not found in swagger", 501, "Not Implemented");
    }

    @Override
    public CompletableFuture<Receipt> getReceipt(String claimId) throws NhifApiException {
        // TODO: Find correct endpoint from swagger or documentation
        throw new NhifApiException("getReceipt endpoint not yet implemented - endpoint not found in swagger", 501, "Not Implemented");
    }

    @Override
    public CompletableFuture<ConfirmationResponse> sendConfirmationCode(String phoneNumber) throws NhifApiException {
        // TODO: Find correct endpoint from swagger or documentation
        throw new NhifApiException("sendConfirmationCode endpoint not yet implemented - endpoint not found in swagger", 501, "Not Implemented");
    }

    @Override
    public CompletableFuture<MonthlyClaimResponse> submitMonthlyClaim(MonthlyClaim request) throws NhifApiException {
        // TODO: Find correct endpoint from swagger or documentation
        throw new NhifApiException("submitMonthlyClaim endpoint not yet implemented - endpoint not found in swagger", 501, "Not Implemented");
    }

    // Reference Data APIs implementation
    @Override
    public CompletableFuture<List<PointOfCare>> getPointsOfCare() throws NhifApiException {
        return get("/api/Reference/GetPointsOfCare", new TypeReference<List<PointOfCare>>() {});
    }

    @Override
    public CompletableFuture<List<VisitType>> getVisitTypes() throws NhifApiException {
        return get("/api/Verification/GetVisitTypes", new TypeReference<List<VisitType>>() {});
    }

    @Override
    public CompletableFuture<List<Facility>> getFacilities() throws NhifApiException {
        return get("/api/Reference/GetFacilities", new TypeReference<List<Facility>>() {});
    }

    // Extended Verification APIs implementation
    @Override
    public CompletableFuture<CardDetails> getCardDetailsByNIN(String nationalId) throws NhifApiException {
        return get("/api/Verification/GetCardDetailsByNIN?nationalID=" + nationalId, CardDetails.class);
    }

    @Override
    public CompletableFuture<PercentCovered> getPercentCovered(String authorizationNo, String itemCode) throws NhifApiException {
        return get("/api/Verification/GetPercentCovered?authorizationNo=" + authorizationNo + "&itemCode=" + itemCode, PercentCovered.class);
    }

    @Override
    public CompletableFuture<EligibilityCheck> checkEligibility(String cardNo, String itemCode) throws NhifApiException {
        return get("/api/Approvals/CheckEligibility?cardNo=" + cardNo + "&itemCode=" + itemCode, EligibilityCheck.class);
    }

    @Override
    public String getCurrentToken() {
        TokenInfo tokenInfo = currentToken.get();
        return tokenInfo != null ? tokenInfo.getToken() : null;
    }

    @SneakyThrows
    @Override
    public void refreshToken() throws NhifApiException {
        final String requestId = String.format("TOKEN-%08x", System.currentTimeMillis() & 0xfffffff);
        
        // Check if we already have a valid token
        TokenInfo current = currentToken.get();
        if (current != null && !current.isExpired()) {
            logger.debug("{} - Using cached token (expires at: {})", requestId, current.getExpiresAt());
            return;
        }
        
        // Synchronize to prevent multiple threads from refreshing the token at the same time
        synchronized (tokenLock) {
            // Double-check after acquiring the lock
            current = currentToken.get();
            if (current != null && !current.isExpired()) {
                logger.debug("{} - Another thread refreshed the token (expires at: {})", 
                    requestId, current.getExpiresAt());
                return;
            }
            
            try {
                // Build form data
                String formData = String.format(
                    "grant_type=client_credentials&scope=%s&client_id=%s&client_secret=%s&username=%s",
                    TOKEN_SCOPE,
                    config.getClientId(),
                    config.getClientSecret(),
                    config.getUsername()
                );
                
                // Create request
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getAuthBaseUrl() + TOKEN_ENDPOINT))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(formData))
                    .build();
                
                // Log the request (with redacted sensitive data)
                String sanitizedFormData = formData
                    .replaceAll("client_secret=[^&]*", "client_secret=[REDACTED]")
                    .replaceAll("username=[^&]*", "username=[REDACTED]");
                String loggableRequest = String.format("POST %s %s", 
                    config.getAuthBaseUrl() + TOKEN_ENDPOINT, 
                    sanitizedFormData);
                    
                logger.debug("{} - Token request: {}", requestId, loggableRequest);
                
                // Send request
                long startTime = System.currentTimeMillis();
                HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
                );
                long duration = System.currentTimeMillis() - startTime;
                
                // Log response status
                logger.debug("{} - Token response status: {} ({} ms)", requestId, response.statusCode(), duration);
                
                
                if (response.statusCode() >= 400) {
                    String errorMsg = String.format("Failed to get token. Status: %d, Response: %s", 
                        response.statusCode(), response.body());
                    logger.error("{} - {}", requestId, errorMsg);
                    throw new NhifApiException(errorMsg, response.statusCode(), response.body());
                }
                
                // Parse token response
                TokenResponse tokenResponse = objectMapper.readValue(response.body(), TokenResponse.class);
                
                TokenInfo newToken = new TokenInfo(
                    tokenResponse.getAccessToken(), 
                    tokenResponse.getExpiresIn()
                );
                
                
                // Update current token and save to disk
                currentToken.set(newToken);
                tokenPersistence.saveToken(newToken);
                
                // Log success (without exposing the actual token)
                logger.info("{} - Successfully obtained access token, expires at {}", 
                    requestId, newToken.getExpiresAt());
                
                // Schedule token refresh
                scheduleTokenRefresh(newToken.getExpiresAt());
                
                logger.debug("{} - Next token refresh scheduled", requestId);
                
            } catch (Exception e) {
                String errorMsg = String.format("Failed to parse token response: %s", e.getMessage());
                logger.error("{} - {}", requestId, errorMsg, e);
                throw new NhifApiException(errorMsg, 500, e.getMessage());
            }
        }
    }

    @Override
    public NhifApiConfig getConfig() {
        return config;
    }

    @Override
    public void close() {
        closed = true;
        if (tokenRefreshFuture != null) {
            tokenRefreshFuture.cancel(true);
        }
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Schedules the next token refresh.
     * @param expiresAt When the current token expires
     */
    @SneakyThrows
    private void scheduleTokenRefresh(Instant expiresAt) {
        // Calculate seconds until 2 minutes before expiration (more reasonable buffer)
        long secondsUntilRefresh = Duration.between(Instant.now(), expiresAt)
            .minus(Duration.ofMinutes(2))
            .getSeconds();
            
        // Ensure we don't schedule in the past or too close to expiration
        secondsUntilRefresh = Math.max(30, secondsUntilRefresh); // At least 30 seconds
            
        if (tokenRefreshFuture != null) {
            tokenRefreshFuture.cancel(false);
        }
        
        tokenRefreshFuture = scheduler.schedule(
            () -> {
                try {
                    refreshToken();
                } catch (NhifApiException e) {
                    logger.error("Failed to refresh token automatically", e);
                }
            },
            secondsUntilRefresh,
            TimeUnit.SECONDS
        );
        
        logger.debug("Next token refresh scheduled in {} seconds (expires at {})", 
            secondsUntilRefresh, expiresAt);
    }
    
    /**
     * Clears the saved token (e.g., on logout or when token is invalidated).
     */
    public void clearToken() {
        tokenPersistence.deleteToken();
        currentToken.set(null);
    }

    // Helper methods for HTTP requests
    
    // ... (rest of the code remains the same)
    private <T> CompletableFuture<T> get(String path, Class<T> responseType) throws NhifApiException {
        return withToken().thenCompose(token -> {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getServiceBaseUrl() + path))
                    .header("Authorization", token)
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            return sendAsync(request, responseType);
        });
    }
    
    private <T> CompletableFuture<T> get(String path, TypeReference<T> typeReference) throws NhifApiException {
        return withToken().thenCompose(token -> {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getServiceBaseUrl() + path))
                    .header("Authorization", token)
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            return sendAsync(request, typeReference);
        });
    }
    
    private <T, R> CompletableFuture<R> post(String path, T requestBody, Class<R> responseType) {
        return withToken().thenCompose(token -> {
            try {
                String body = objectMapper.writeValueAsString(requestBody);
                String requestId = String.format("REQ-%08x", System.identityHashCode(body));
                
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getServiceBaseUrl() + path))
                    .header("Authorization", token)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
                return sendAsync(request, responseType);
            } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
            }
        });
    }
    
    private <T> CompletableFuture<T> sendAsync(HttpRequest request, Class<T> responseType) {
        final String requestId = String.format("REQ-%08x", System.identityHashCode(request));
        final long startTime = System.currentTimeMillis();
        
        logRequest(request, requestId);
        
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    long duration = System.currentTimeMillis() - startTime;
                    logResponse(request, requestId, response, duration);
                    
                    if (response.statusCode() >= 200 && response.statusCode() < 300) {
                        try {
                            T result = objectMapper.readValue(response.body(), responseType);
                            logger.debug("{} - Successfully parsed response to {}", requestId, responseType.getSimpleName());
                            return result;
                        } catch (Exception e) {
                            String errorMsg = String.format("%s - Failed to parse response: %s", requestId, e.getMessage());
                            logger.error(errorMsg, e);
                            throw new CompletionException(errorMsg, e);
                        }
                    } else {
                        String errorMsg = String.format("%s - Request failed with status %d: %s", 
                            requestId, response.statusCode(), response.body());
                        logger.error(errorMsg);
                        throw new CompletionException(new NhifApiException(errorMsg));
                    }
                })
                .exceptionally(e -> {
                    logger.error("{} - Request failed: {}", requestId, e.getMessage(), e);
                    throw new CompletionException(e);
                });
    }
    
    private <T> CompletableFuture<T> sendAsync(HttpRequest request, TypeReference<T> typeReference) {
        final String requestId = String.format("REQ-%08x", System.identityHashCode(request));
        final long startTime = System.currentTimeMillis();
        
        logRequest(request, requestId);
        
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    long duration = System.currentTimeMillis() - startTime;
                    logResponse(request, requestId, response, duration);
                    
                    if (response.statusCode() >= 200 && response.statusCode() < 300) {
                        try {
                            T result = objectMapper.readValue(response.body(), typeReference);
                            logger.debug("{} - Successfully parsed response to {}", requestId, typeReference.getType().getTypeName());
                            return result;
                        } catch (Exception e) {
                            String errorMsg = String.format("%s - Failed to parse response: %s", requestId, e.getMessage());
                            logger.error(errorMsg, e);
                            throw new CompletionException(errorMsg, e);
                        }
                    } else {
                        String errorMsg = String.format("%s - Request failed with status %d: %s", 
                            requestId, response.statusCode(), response.body());
                        logger.error(errorMsg);
                        throw new CompletionException(new NhifApiException(errorMsg));
                    }
                })
                .exceptionally(e -> {
                    logger.error("{} - Request failed: {}", requestId, e.getMessage(), e);
                    throw new CompletionException(e);
                });
    }
    
    /**
     * Helper method to log HTTP request details
     */
    private void logRequest(HttpRequest request, String requestId) {
        if (logger.isDebugEnabled()) {
            StringBuilder logMsg = new StringBuilder();
            logMsg.append(String.format("%s - Sending %s request to: %s\n", 
                requestId, request.method(), request.uri()));
                
            // Log headers (redacting sensitive information)
            request.headers().map()
                .forEach((name, values) -> 
                    values.forEach(value -> {
                        if (name != null && name.equalsIgnoreCase("authorization")) {
                            logMsg.append(String.format("%s: %s\n", name, "[REDACTED]"));
                        } else {
                            logMsg.append(String.format("%s: %s\n", name, value));
                        }
                    }));
                
            // For requests with body
            if (request.bodyPublisher().isPresent()) {
                logMsg.append("[Request body present]\n");
            }
            
            logger.debug(logMsg.toString());
        }
    }
    
    /**
     * Helper method to log HTTP response details
     */
    private void logResponse(HttpRequest request, String requestId, HttpResponse<String> response, long durationMs) {
        if (logger.isDebugEnabled()) {
            StringBuilder logMsg = new StringBuilder();
            logMsg.append(String.format("%s - Received response in %d ms - Status: %d %s %s\n", 
                requestId, 
                durationMs, 
                response.statusCode(),
                request.method(),
                request.uri().getPath()));
                
            // Log response headers (redacting sensitive information)
            response.headers().map()
                .forEach((name, values) -> 
                    values.forEach(value -> {
                        if (name != null && name.toLowerCase().contains("authorization")) {
                            logMsg.append(String.format("%s: %s\n", name, "[REDACTED]"));
                        } else {
                            logMsg.append(String.format("%s: %s\n", name, value));
                        }
                    }));
                
            // Log response body (first 1000 chars to avoid huge logs)
            if (response.body() != null && !response.body().isEmpty()) {
                String bodyPreview = response.body().length() > 1000 
                    ? response.body().substring(0, 1000) + "... [truncated]" 
                    : response.body();
                logMsg.append("Response body: ").append(bodyPreview).append("\n");
            }
            
            logger.debug(logMsg.toString());
        }
    }
    
    private CompletableFuture<String> withToken() {
        TokenInfo tokenInfo = currentToken.get();
        if (tokenInfo == null || tokenInfo.isExpired()) {
            try {
                refreshToken();
                tokenInfo = currentToken.get();
                if (tokenInfo == null) {
                    return CompletableFuture.failedFuture(new NhifApiException("Failed to obtain access token"));
                }
                return CompletableFuture.completedFuture("Bearer " + tokenInfo.getToken());
            } catch (NhifApiException e) {
                return CompletableFuture.failedFuture(e);
            }
        }
        return CompletableFuture.completedFuture("Bearer " + tokenInfo.getToken());
    }
    
    // Token response DTO
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    private static class TokenResponse {
        @com.fasterxml.jackson.annotation.JsonProperty("access_token")
        private String accessToken;
        
        @com.fasterxml.jackson.annotation.JsonProperty("token_type")
        private String tokenType;
        
        @com.fasterxml.jackson.annotation.JsonProperty("expires_in")
        private int expiresIn;
        
        public String getAccessToken() {
            return accessToken;
        }
        
        public String getTokenType() {
            return tokenType;
        }
        
        public int getExpiresIn() {
            return expiresIn;
        }
        
        // Setters needed for Jackson deserialization
        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
        
        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }
        
        public void setExpiresIn(int expiresIn) {
            this.expiresIn = expiresIn;
        }
    }
}
