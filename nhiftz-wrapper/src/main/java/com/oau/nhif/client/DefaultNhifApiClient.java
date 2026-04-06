package com.oau.nhif.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oau.nhif.client.model.*;
import com.oau.nhif.exception.NhifApiException;
import lombok.SneakyThrows;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.CompletionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Apache HttpClient imports for Java 1.8 compatibility
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.Header;

/**
 * Default implementation of the NHIF API client.
 */
public class DefaultNhifApiClient implements NhifApiClient {
    private static final String DEFAULT_TOKEN_ENDPOINT = "/authserver/connect/token";
    private static final String TOKEN_SCOPE = "OnlineServices";
    private static final Logger logger = LoggerFactory.getLogger(DefaultNhifApiClient.class);
    
    private final NhifApiConfig config;
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final ScheduledExecutorService scheduler;
    private final AtomicReference<TokenInfo> currentToken = new AtomicReference<>();
    private final TokenPersistence tokenPersistence;
    private ScheduledFuture<?> tokenRefreshFuture;
    private boolean closed = false;
    private final Object tokenLock = new Object();

    public DefaultNhifApiClient(NhifApiConfig config) throws NhifApiException {
        this.config = config;
        
        // Build HTTP client with timeout configuration
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout((int) config.getConnectionTimeout().toMillis())
                .setSocketTimeout((int) config.getReadTimeout().toMillis())
                .build();
        
        try {
            HttpClientBuilder clientBuilder = HttpClientBuilder.create()
                    .setDefaultRequestConfig(requestConfig);
            
            // Configure SSL if needed
            if (config.isSkipSslVerification()) {
                // Create trust strategy that accepts all certificates
                TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
                
                // Build SSL context
                SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                    new SSLContextBuilder()
                        .loadTrustMaterial(null, acceptingTrustStrategy)
                        .build(),
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                
                clientBuilder.setSSLSocketFactory(sslConnectionSocketFactory);
            }
            
            this.httpClient = clientBuilder.build();
        } catch (Exception e) {
            throw new NhifApiException("Failed to initialize HTTP client: " + e.getMessage(), e);
        }
        
        // Configure ObjectMapper with appropriate settings
        this.objectMapper = new ObjectMapper()
                .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
                
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
    public CompletableFuture<CardAuthorizationResponse> authorizeCardWithBiometric(CardAuthorizationRequest request) throws NhifApiException {
        return post("/api/Verification/AuthorizeCard", request, CardAuthorizationResponse.class);
    }
    
    @Override
    public CompletableFuture<CardAuthorizationResponse> authorizeCardSimple(String cardNumber, int visitTypeID) throws NhifApiException {
        SimpleCardAuthorizationRequest request = new SimpleCardAuthorizationRequest(cardNumber, visitTypeID);
        return authorizeCardSimple(request);
    }
    
    @Override
    public CompletableFuture<CardAuthorizationResponse> authorizeCardSimple(SimpleCardAuthorizationRequest request) throws NhifApiException {
        return post("/api/Verification/AuthorizeCard", request, CardAuthorizationResponse.class);
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
                .thenApply(visit -> visit != null ? Arrays.asList(visit) : Collections.emptyList());
    }

    @Override
    public CompletableFuture<MedicalHistory> getMedicalHistory(String cardNumber) throws NhifApiException {
        // Add daysPast parameter (default 60 from swagger)
        return get("/api/History/GetMedicalHistory?cardNo=" + cardNumber + "&daysPast=60", MedicalHistory.class);
    }

    // Claims APIs - routed to OCS (Online Claim Submission) service

    @Override
    public CompletableFuture<ClaimTest> testClaim() throws NhifApiException {
        return ocsGet("/api/Claims/Test", ClaimTest.class);
    }

    @Override
    public CompletableFuture<ClaimSubmissionResponse> submitFolio(FolioSubmissionRequest request) throws NhifApiException {
        return ocsPost("/api/Claims/SubmitFolio", request, ClaimSubmissionResponse.class);
    }

    @Override
    public CompletableFuture<SubmittedClaim> getSubmittedClaim(String claimId) throws NhifApiException {
        // TODO: Find correct endpoint from swagger or documentation
        throw new NhifApiException("getSubmittedClaim endpoint not yet implemented - endpoint not found in swagger", 501, "Not Implemented");
    }

    @Override
    public CompletableFuture<List<ClaimSubmission>> getSubmittedClaims(String facilityCode, int claimYear, int claimMonth) throws NhifApiException {
        return ocsGet("/api/Claims/GetSubmittedClaims?facilityCode=" + facilityCode + "&claimYear=" + claimYear + "&claimMonth=" + claimMonth, new TypeReference<List<ClaimSubmission>>() {});
    }

    @Override
    public CompletableFuture<Receipt> getReceipt(String claimId) throws NhifApiException {
        // TODO: Find correct endpoint from swagger or documentation
        throw new NhifApiException("getReceipt endpoint not yet implemented - endpoint not found in swagger", 501, "Not Implemented");
    }

    @Override
    public CompletableFuture<GetReceiptResponse> getReceipt(String facilityCode, int claimYear, int claimMonth, String folioNo) throws NhifApiException {
        String endpoint = String.format("/api/Claims/GetReceipt?facilityCode=%s&claimYear=%d&claimMonth=%d&folioNo=%s",
                                      facilityCode, claimYear, claimMonth, folioNo);
        return ocsGet(endpoint, new TypeReference<GetReceiptResponse>() {});
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

    @Override
    public CompletableFuture<MonthlyClaimSubmissionResponse> submitMonthlyClaimSubmission(MonthlyClaimSubmission request) throws NhifApiException {
        return ocsPost("/api/Claims/SubmitMonthlyClaim", request, MonthlyClaimSubmissionResponse.class);
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

    @Override
    public CompletableFuture<List<Disease>> getDiseases() throws NhifApiException {
        return get("/api/Reference/GetDiseases", new TypeReference<List<Disease>>() {});
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

    // Admissions APIs implementation
    @Override
    public CompletableFuture<List<AdmissionType>> getAdmissionTypes() throws NhifApiException {
        return get("/api/Admissions/GetAdmissionTypes", new TypeReference<List<AdmissionType>>() {});
    }

    @Override
    public CompletableFuture<List<DischargeType>> getDischargeTypes() throws NhifApiException {
        return get("/api/Admissions/GetDischargeTypes", new TypeReference<List<DischargeType>>() {});
    }

    @Override
    public CompletableFuture<List<WardType>> getWardTypes() throws NhifApiException {
        return get("/api/Admissions/GetWardTypes", new TypeReference<List<WardType>>() {});
    }

    @Override
    public CompletableFuture<List<RoomType>> getRoomTypes() throws NhifApiException {
        return get("/api/Admissions/GetRoomTypes", new TypeReference<List<RoomType>>() {});
    }

    @Override
    public CompletableFuture<GenericResponse> getDetailsByAuthorizationNo(String authorizationNo) throws NhifApiException {
        return get("/api/Admissions/GetDetailsByAuthorizationNo?authorizationNo=" + authorizationNo, GenericResponse.class);
    }

    @Override
    public CompletableFuture<GenericResponse> getAdmissionDetailsByAuthorizationNo(String authorizationNo) throws NhifApiException {
        return get("/api/Admissions/GetAdmissionDetailsByAuthorizationNo?authorizationNo=" + authorizationNo, GenericResponse.class);
    }

    @Override
    public CompletableFuture<List<AdmissionType>> getAdmissionTypesByProductCode(String productCode) throws NhifApiException {
        return get("/api/Admissions/GetAdmissionTypesByProductCode?productCode=" + productCode, new TypeReference<List<AdmissionType>>() {});
    }

    @Override
    public CompletableFuture<GenericResponse> admitPatient(PatientAdmissionModel request) throws NhifApiException {
        return post("/api/Admissions/AdmitPatient", request, GenericResponse.class);
    }

    @Override
    public CompletableFuture<GenericResponse> dischargePatient(PatientDischargeModel request) throws NhifApiException {
        return post("/api/Admissions/DishargePatient", request, GenericResponse.class);
    }

    @Override
    public CompletableFuture<GenericResponse> transferPatient(PatientTransferModel request) throws NhifApiException {
        return post("/api/Admissions/TransferPatient", request, GenericResponse.class);
    }

    @Override
    public CompletableFuture<List<AdmittedPatient>> getAdmittedPatients() throws NhifApiException {
        return get("/api/Admissions/GetAdmittedPatients", new TypeReference<List<AdmittedPatient>>() {});
    }

    @Override
    public CompletableFuture<List<AdmittedPatient>> getAdmittedPatientsByFacility(String facilityCode) throws NhifApiException {
        return get("/api/Admissions/GetAdmittedPatientsByFacility?facilityCode=" + facilityCode, new TypeReference<List<AdmittedPatient>>() {});
    }

    // Packages APIs implementation
    @Override
    public CompletableFuture<List<PackageItem>> getPackageItems() throws NhifApiException {
        return get("/api/Packages/GetItems", new TypeReference<List<PackageItem>>() {});
    }

    @Override
    public CompletableFuture<List<ItemType>> getItemTypes() throws NhifApiException {
        return get("/api/Packages/GetItemTypes", new TypeReference<List<ItemType>>() {});
    }

    @Override
    public CompletableFuture<List<BenefitScheme>> getBenefitSchemes() throws NhifApiException {
        return get("/api/Packages/GetBenefitSchemes", new TypeReference<List<BenefitScheme>>() {});
    }

    @Override
    public CompletableFuture<List<PricePackage>> getPricePackages() throws NhifApiException {
        return get("/api/Packages/GetPricePackages", new TypeReference<List<PricePackage>>() {});
    }

    @Override
    public CompletableFuture<List<FacilityPackageItem>> getPricePackageByFacility(String facilityCode) throws NhifApiException {
        return get("/api/Packages/GetPricePackage?facilityCode=" + facilityCode, new TypeReference<List<FacilityPackageItem>>() {});
    }

    @Override
    public CompletableFuture<GenericResponse> loginPractitioner(PractitionerAttendanceRequest request) throws NhifApiException {
        return post("/api/Attendance/LoginPractitioner", request, GenericResponse.class);
    }

    @Override
    public CompletableFuture<GenericResponse> logoutPractitioner(String practitionerNo, String facilityCode) throws NhifApiException {
        return post("/api/Attendance/LogoutPractitioner?practitionerNo=" + practitionerNo + "&facilityCode=" + facilityCode, 
                   null, GenericResponse.class);
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
                String tokenEndpoint = config.getTokenEndpoint() != null ? config.getTokenEndpoint() : DEFAULT_TOKEN_ENDPOINT;
                HttpPost request = new HttpPost(config.getAuthBaseUrl() + tokenEndpoint);
                request.setHeader("Content-Type", "application/x-www-form-urlencoded");
                request.setEntity(new StringEntity(formData, ContentType.APPLICATION_FORM_URLENCODED));
                
                // Log the request (with redacted sensitive data)
                String sanitizedFormData = formData
                    .replaceAll("client_secret=[^&]*", "client_secret=[REDACTED]")
                    .replaceAll("username=[^&]*", "username=[REDACTED]");
                String loggableRequest = String.format("POST %s %s", 
                    config.getAuthBaseUrl() + tokenEndpoint,
                    sanitizedFormData);
                    
                logger.debug("{} - Token request: {}", requestId, loggableRequest);
                
                // Send request
                long startTime = System.currentTimeMillis();
                CloseableHttpResponse response = httpClient.execute(request);
                long duration = System.currentTimeMillis() - startTime;
                
                try {
                    // Log response status
                    logger.debug("{} - Token response status: {} ({} ms)", requestId, response.getStatusLine().getStatusCode(), duration);
                    
                    String responseBody = EntityUtils.toString(response.getEntity());
                    
                    if (response.getStatusLine().getStatusCode() >= 400) {
                        String errorMsg = String.format("Failed to get token. Status: %d, Response: %s", 
                            response.getStatusLine().getStatusCode(), responseBody);
                        logger.error("{} - {}", requestId, errorMsg);
                        throw new NhifApiException(errorMsg, response.getStatusLine().getStatusCode(), responseBody);
                    }
                    
                    // Parse token response
                    TokenResponse tokenResponse = objectMapper.readValue(responseBody, TokenResponse.class);
                    
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
                    
                } finally {
                    response.close();
                }
                
            } catch (Exception e) {
                String errorMsg = String.format("Failed to get token: %s", e.getMessage());
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
        
        // Close HTTP client
        try {
            httpClient.close();
        } catch (Exception e) {
            logger.warn("Error closing HTTP client", e);
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

    // Helper methods for HTTP requests (ServiceHub)

    private <T> CompletableFuture<T> get(String path, Class<T> responseType) throws NhifApiException {
        return getFrom(config.getServiceBaseUrl(), path, responseType);
    }

    private <T> CompletableFuture<T> get(String path, TypeReference<T> typeReference) throws NhifApiException {
        return getFrom(config.getServiceBaseUrl(), path, typeReference);
    }

    private <T, R> CompletableFuture<R> post(String path, T requestBody, Class<R> responseType) {
        return postTo(config.getServiceBaseUrl(), path, requestBody, responseType);
    }

    // Helper methods for HTTP requests (OCS - Online Claim Submission)

    private <T> CompletableFuture<T> ocsGet(String path, Class<T> responseType) throws NhifApiException {
        return getFrom(config.getOcsBaseUrl(), path, responseType);
    }

    private <T> CompletableFuture<T> ocsGet(String path, TypeReference<T> typeReference) throws NhifApiException {
        return getFrom(config.getOcsBaseUrl(), path, typeReference);
    }

    private <T, R> CompletableFuture<R> ocsPost(String path, T requestBody, Class<R> responseType) {
        return postTo(config.getOcsBaseUrl(), path, requestBody, responseType);
    }

    // Base HTTP helper methods

    private <T> CompletableFuture<T> getFrom(String baseUrl, String path, Class<T> responseType) {
        return withToken().thenCompose(token -> {
            HttpGet request = new HttpGet(baseUrl + path);
            request.setHeader("Authorization", token);
            request.setHeader("Accept", "application/json");
            return sendAsync(request, responseType);
        });
    }

    private <T> CompletableFuture<T> getFrom(String baseUrl, String path, TypeReference<T> typeReference) {
        return withToken().thenCompose(token -> {
            HttpGet request = new HttpGet(baseUrl + path);
            request.setHeader("Authorization", token);
            request.setHeader("Accept", "application/json");
            return sendAsync(request, typeReference);
        });
    }

    private <T, R> CompletableFuture<R> postTo(String baseUrl, String path, T requestBody, Class<R> responseType) {
        return withToken().thenCompose(token -> {
            try {
                String body = objectMapper.writeValueAsString(requestBody);

                HttpPost request = new HttpPost(baseUrl + path);
                request.setHeader("Authorization", token);
                request.setHeader("Content-Type", "application/json");
                request.setHeader("Accept", "application/json");
                request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));

                return sendAsync(request, responseType);
            } catch (Exception e) {
                CompletableFuture<R> future = new CompletableFuture<>();
                future.completeExceptionally(e);
                return future;
            }
        });
    }
    
    private <T> CompletableFuture<T> sendAsync(HttpUriRequest request, Class<T> responseType) {
        final String requestId = String.format("REQ-%08x", System.identityHashCode(request));
        final long startTime = System.currentTimeMillis();
        
        logRequest(request, requestId);
        
        return CompletableFuture.supplyAsync(() -> {
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                long duration = System.currentTimeMillis() - startTime;
                String responseBody = EntityUtils.toString(response.getEntity());
                logResponse(request, requestId, response, responseBody, duration);
                
                if (response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300) {
                    try {
                        T result = objectMapper.readValue(responseBody, responseType);
                        logger.debug("{} - Successfully parsed response to {}", requestId, responseType.getSimpleName());
                        return result;
                    } catch (Exception e) {
                        String errorMsg = String.format("%s - Failed to parse response: %s", requestId, e.getMessage());
                        logger.error(errorMsg, e);
                        throw new CompletionException(errorMsg, e);
                    }
                } else {
                    String errorMsg = String.format("%s - Request failed with status %d: %s", 
                        requestId, response.getStatusLine().getStatusCode(), responseBody);
                    logger.error(errorMsg);
                    throw new CompletionException(new NhifApiException(errorMsg));
                }
            } catch (Exception e) {
                logger.error("{} - Request failed: {}", requestId, e.getMessage(), e);
                throw new CompletionException(e);
            }
        });
    }
    
    private <T> CompletableFuture<T> sendAsync(HttpUriRequest request, TypeReference<T> typeReference) {
        final String requestId = String.format("REQ-%08x", System.identityHashCode(request));
        final long startTime = System.currentTimeMillis();
        
        logRequest(request, requestId);
        
        return CompletableFuture.supplyAsync(() -> {
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                long duration = System.currentTimeMillis() - startTime;
                String responseBody = EntityUtils.toString(response.getEntity());
                logResponse(request, requestId, response, responseBody, duration);
                
                if (response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300) {
                    try {
                        T result = objectMapper.readValue(responseBody, typeReference);
                        logger.debug("{} - Successfully parsed response to {}", requestId, typeReference.getType().getTypeName());
                        return result;
                    } catch (Exception e) {
                        String errorMsg = String.format("%s - Failed to parse response: %s", requestId, e.getMessage());
                        logger.error(errorMsg, e);
                        throw new CompletionException(errorMsg, e);
                    }
                } else {
                    String errorMsg = String.format("%s - Request failed with status %d: %s", 
                        requestId, response.getStatusLine().getStatusCode(), responseBody);
                    logger.error(errorMsg);
                    throw new CompletionException(new NhifApiException(errorMsg));
                }
            } catch (Exception e) {
                logger.error("{} - Request failed: {}", requestId, e.getMessage(), e);
                throw new CompletionException(e);
            }
        });
    }
    
    /**
     * Helper method to log HTTP request details
     */
    private void logRequest(HttpUriRequest request, String requestId) {
        if (logger.isDebugEnabled()) {
            StringBuilder logMsg = new StringBuilder();
            logMsg.append(String.format("%s - Sending %s request to: %s\n", 
                requestId, request.getMethod(), request.getURI()));
                
            // Log headers (redacting sensitive information)
            for (Header header : request.getAllHeaders()) {
                if (header.getName() != null && header.getName().equalsIgnoreCase("authorization")) {
                    logMsg.append(String.format("%s: %s\n", header.getName(), "[REDACTED]"));
                } else {
                    logMsg.append(String.format("%s: %s\n", header.getName(), header.getValue()));
                }
            }
            
            logger.debug(logMsg.toString());
        }
    }
    
    /**
     * Helper method to log HTTP response details
     */
    private void logResponse(HttpUriRequest request, String requestId, CloseableHttpResponse response, String responseBody, long durationMs) {
        if (logger.isDebugEnabled()) {
            StringBuilder logMsg = new StringBuilder();
            logMsg.append(String.format("%s - Received response in %d ms - Status: %d %s %s\n", 
                requestId, 
                durationMs, 
                response.getStatusLine().getStatusCode(),
                request.getMethod(),
                request.getURI().getPath()));
                
            // Log response headers (redacting sensitive information)
            for (Header header : response.getAllHeaders()) {
                if (header.getName() != null && header.getName().toLowerCase().contains("authorization")) {
                    logMsg.append(String.format("%s: %s\n", header.getName(), "[REDACTED]"));
                } else {
                    logMsg.append(String.format("%s: %s\n", header.getName(), header.getValue()));
                }
            }
                
            // Log response body (first 1000 chars to avoid huge logs)
            if (responseBody != null && !responseBody.isEmpty()) {
                String bodyPreview = responseBody.length() > 1000 
                    ? responseBody.substring(0, 1000) + "... [truncated]" 
                    : responseBody;
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
                    CompletableFuture<String> future = new CompletableFuture<>();
                    future.completeExceptionally(new NhifApiException("Failed to obtain access token"));
                    return future;
                }
                CompletableFuture<String> future = new CompletableFuture<>();
                future.complete("Bearer " + tokenInfo.getToken());
                return future;
            } catch (NhifApiException e) {
                CompletableFuture<String> future = new CompletableFuture<>();
                future.completeExceptionally(e);
                return future;
            }
        }
        CompletableFuture<String> future = new CompletableFuture<>();
        future.complete("Bearer " + tokenInfo.getToken());
        return future;
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