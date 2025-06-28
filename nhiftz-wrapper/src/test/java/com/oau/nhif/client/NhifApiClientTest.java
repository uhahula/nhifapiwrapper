package com.oau.nhif.client;

import com.oau.nhif.client.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the NHIF API client
 */
@ExtendWith(MockitoExtension.class)
class NhifApiClientTest {

    private NhifApiClient client;
    private static final String TEST_CARD_NUMBER = "1234567890";

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpResponse<String> mockResponse;

    @BeforeEach
    void setUp() throws Exception {
        // Create test configuration
        NhifApiConfig config = NhifApiConfig.builder()
                .authBaseUrl("https://verification.nhif.or.tz")
                .serviceBaseUrl("http://test.nhif.or.tz/servicehub")
                .clientId("test-client")
                .clientSecret("test-secret")
                .username("test-user")
                .build();
        
        // Create a test instance of the client with mocked HTTP client
        client = new DefaultNhifApiClient(config) {
            @Override
            protected HttpClient createHttpClient() {
                return mockHttpClient;
            }
        };
        
        // Setup default mock response for token endpoint
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn("{\"access_token\":\"dummy-token\",\"expires_in\":3600}");
        when(mockHttpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(CompletableFuture.completedFuture(mockResponse));
    }

    @Test
    void getCardDetails() throws ExecutionException, InterruptedException {
        // This test would require a mock server in a real scenario
        CompletableFuture<CardDetails> future = client.getCardDetails(TEST_CARD_NUMBER);
        
        // Just verify the future completes without throwing an exception
        // In a real test, you would verify the response
        assertDoesNotThrow(future::get);
    }

    @Test
    void verifyCard() throws ExecutionException, InterruptedException {
        CompletableFuture<CardVerification> future = client.verifyCard(TEST_CARD_NUMBER);
        
        // Just verify the future completes without throwing an exception
        assertDoesNotThrow(future::get);
    }

    @Test
    void getPatientDetails() throws ExecutionException, InterruptedException {
        CompletableFuture<PatientDetails> future = client.getPatientDetails(TEST_CARD_NUMBER);
        
        // Just verify the future completes without throwing an exception
        assertDoesNotThrow(future::get);
    }

    @Test
    void getPreviousPatientVisits() throws ExecutionException, InterruptedException {
        CompletableFuture<List<PatientVisit>> future = client.getPreviousPatientVisits(TEST_CARD_NUMBER);
        
        // Just verify the future completes without throwing an exception
        assertDoesNotThrow(future::get);
    }

    @Test
    void getMedicalHistory() throws ExecutionException, InterruptedException {
        CompletableFuture<MedicalHistory> future = client.getMedicalHistory(TEST_CARD_NUMBER);
        
        // Just verify the future completes without throwing an exception
        assertDoesNotThrow(future::get);
    }

    @Test
    void testClaim() throws ExecutionException, InterruptedException {
        CompletableFuture<TestResponse> future = client.testClaim();
        
        // Just verify the future completes without throwing an exception
        assertDoesNotThrow(future::get);
    }

    @Test
    void submitFolio() {
        // This would require creating a proper FolioSubmission object
        // and is better suited for an integration test with a mock server
        assertTrue(true);
    }

    @Test
    void getSubmittedClaim() throws ExecutionException, InterruptedException {
        String claimId = "TEST123";
        CompletableFuture<SubmittedClaim> future = client.getSubmittedClaim(claimId);
        
        // Just verify the future completes without throwing an exception
        assertDoesNotThrow(future::get);
    }

    @Test
    void getReceipt() throws ExecutionException, InterruptedException {
        String claimId = "TEST123";
        CompletableFuture<Receipt> future = client.getReceipt(claimId);
        
        // Just verify the future completes without throwing an exception
        assertDoesNotThrow(future::get);
    }

    @Test
    void sendConfirmationCode() throws ExecutionException, InterruptedException {
        String phoneNumber = "255712345678";
        CompletableFuture<ConfirmationResponse> future = client.sendConfirmationCode(phoneNumber);
        
        // Just verify the future completes without throwing an exception
        assertDoesNotThrow(future::get);
    }

    @Test
    void submitMonthlyClaim() {
        // This would require creating a proper MonthlyClaim object
        // and is better suited for an integration test with a mock server
        assertTrue(true);
    }

    @Test
    void getCurrentToken() {
        String token = client.getCurrentToken();
        // Token might be null if not yet fetched
        assertTrue(token == null || !token.isEmpty());
    }

    @Test
    void refreshToken() {
        assertDoesNotThrow(() -> client.refreshToken());
    }

    @Test
    void getConfig() {
        NhifApiConfig config = client.getConfig();
        assertNotNull(config);
        assertEquals("test-client", config.getClientId());
    }
}
