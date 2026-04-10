package com.oau.nhif.example;

import com.oau.nhif.client.NhifApiClient;
import com.oau.nhif.client.NhifApiClientFactory;
import com.oau.nhif.client.model.CardAuthorizationRequest;
import com.oau.nhif.client.model.CardAuthorizationResponse;
import com.oau.nhif.client.model.CardDetails;
import com.oau.nhif.client.model.CardVerification;
import com.oau.nhif.client.model.VerifyCardRequest;
import com.oau.nhif.exception.NhifApiException;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;

/**
 * Test biometric authentication for card verification using patient ID 19900117-11101-00003-27.
 *
 * Tests both VerifyCard and AuthorizeCard endpoints with three biometric methods:
 *   NONE, FINGERPRINT (fpCode: L1-L5, R1-R5), FACIAL
 */
public class BiometricAuthTest {

    private static final String PATIENT_NIN = "19900117111010000327"; // 19900117-11101-00003-27
    private static final int VERIFIER_ID = 11014;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static void main(String[] args) {
        String authBaseUrl = "https://test.nhif.or.tz";
        String serviceBaseUrl = "https://test.nhif.or.tz/servicehub";
        String clientId = "11014";
        String clientSecret = "ntbzRGbrwwHj8Jwd7bbPsg==";
        String username = "Mtundi";

        try (NhifApiClient client = NhifApiClientFactory.createClient(
                authBaseUrl, serviceBaseUrl, clientId, clientSecret, username)) {

            System.out.println("=== Biometric Authentication Test ===");
            System.out.println("Patient National ID: 19900117-11101-00003-27\n");

            // Step 0: Look up card number from national ID
            System.out.println("--- Step 0: Look up card details by NIN ---");
            String cardNo = null;
            try {
                CardDetails details = client.getCardDetailsByNIN(PATIENT_NIN).get();
                cardNo = details.getCardNo();
                System.out.printf("  Card No:      %s\n", cardNo);
                System.out.printf("  Full Name:    %s\n", details.getFullName());
                System.out.printf("  Card Status:  %s\n", details.getCardStatus());
                System.out.printf("  Product:      %s\n", details.getProductCode());
            } catch (Exception e) {
                System.out.println("  NIN lookup failed: " + e.getMessage());
                cardNo = PATIENT_NIN;
            }
            System.out.println();

            // ============================================================
            // PART A: AuthorizeCard with biometric (proven endpoint)
            // ============================================================
            System.out.println("========== PART A: AuthorizeCard (biometric) ==========\n");

            // Test A1: NONE
            testAuthorizeWithNone(client, cardNo);

            // Test A2: FINGERPRINT with R5
            testAuthorizeWithFingerprint(client, cardNo, "R5");

            // Test A3: FACIAL
            testAuthorizeWithFacial(client, cardNo);

            // ============================================================
            // PART B: VerifyCard with biometric
            // ============================================================
            System.out.println("========== PART B: VerifyCard (biometric) ==========\n");

            // Test B1: NONE
            testVerifyWithNone(client, cardNo);

            // Test B2: FINGERPRINT with R5
            testVerifyWithFingerprint(client, cardNo, "R5");

            // Test B3: FACIAL
            testVerifyWithFacial(client, cardNo);

            System.out.println("\n=== All Biometric Tests Complete ===");

        } catch (NhifApiException e) {
            System.err.println("Client initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ================================================================
    // PART A: AuthorizeCard tests
    // ================================================================

    private static void testAuthorizeWithNone(NhifApiClient client, String cardNo) {
        System.out.println("--- A1: AuthorizeCard biometricMethod=NONE ---");
        CardAuthorizationRequest req = new CardAuthorizationRequest()
                .cardNo(cardNo)
                .biometricMethod("NONE")
                .nationalID(PATIENT_NIN)
                .fpCode("")
                .imageData("")
                .visitTypeID(1)
                .referralNo("")
                .remarks("Biometric test - NONE");
        executeAuthorize(client, req);
    }

    private static void testAuthorizeWithFingerprint(NhifApiClient client, String cardNo, String fpCode) {
        System.out.println("--- A2: AuthorizeCard biometricMethod=FINGERPRINT fpCode=" + fpCode + " ---");
        String template = Base64.getEncoder().encodeToString("SAMPLE_FP_TEMPLATE".getBytes());
        CardAuthorizationRequest req = new CardAuthorizationRequest()
                .cardNo(cardNo)
                .biometricMethod("FINGERPRINT")
                .nationalID(PATIENT_NIN)
                .fpCode(fpCode)
                .imageData(template)
                .visitTypeID(1)
                .referralNo("")
                .remarks("Biometric test - FINGERPRINT");
        executeAuthorize(client, req);
    }

    private static void testAuthorizeWithFacial(NhifApiClient client, String cardNo) {
        System.out.println("--- A3: AuthorizeCard biometricMethod=FACIAL ---");
        String image = Base64.getEncoder().encodeToString("SAMPLE_FACIAL_IMAGE".getBytes());
        CardAuthorizationRequest req = new CardAuthorizationRequest()
                .cardNo(cardNo)
                .biometricMethod("FACIAL")
                .nationalID(PATIENT_NIN)
                .fpCode("")
                .imageData(image)
                .visitTypeID(1)
                .referralNo("")
                .remarks("Biometric test - FACIAL");
        executeAuthorize(client, req);
    }

    private static void executeAuthorize(NhifApiClient client, CardAuthorizationRequest request) {
        try {
            System.out.println("  REQUEST: " + MAPPER.writeValueAsString(request));
            CardAuthorizationResponse resp = client.authorizeCardWithBiometric(request).get();
            System.out.println("  RESPONSE:");
            System.out.printf("    Status:          %s\n", resp.getAuthorizationStatus());
            System.out.printf("    Authorization #: %s\n", resp.getAuthorizationNo());
            System.out.printf("    Patient:         %s\n", resp.getFullName());
            System.out.printf("    Scheme:          %s\n", resp.getSchemeName());
            System.out.printf("    Description:     %s\n", resp.getStatusDescription());
        } catch (Exception e) {
            System.out.println("  FAILED: " + extractMessage(e));
        }
        System.out.println();
    }

    // ================================================================
    // PART B: VerifyCard tests
    // ================================================================

    private static void testVerifyWithNone(NhifApiClient client, String cardNo) {
        System.out.println("--- B1: VerifyCard biometricMethod=NONE ---");
        VerifyCardRequest req = new VerifyCardRequest();
        req.setCardNo(cardNo);
        req.setVerifierID(VERIFIER_ID);
        req.setCardTypeID(1);
        req.setBiometricMethod("NONE");
        req.setFpCode("");
        req.setImageData("");
        req.setVisitTypeID(1);
        req.setReferralNo("");
        req.setRemarks("");
        executeVerify(client, req);
    }

    private static void testVerifyWithFingerprint(NhifApiClient client, String cardNo, String fpCode) {
        System.out.println("--- B2: VerifyCard biometricMethod=FINGERPRINT fpCode=" + fpCode + " ---");
        String template = Base64.getEncoder().encodeToString("SAMPLE_FP_TEMPLATE".getBytes());
        VerifyCardRequest req = new VerifyCardRequest();
        req.setCardNo(cardNo);
        req.setVerifierID(VERIFIER_ID);
        req.setCardTypeID(1);
        req.setBiometricMethod("FINGERPRINT");
        req.setFpCode(fpCode);
        req.setImageData(template);
        req.setVisitTypeID(1);
        req.setReferralNo("");
        req.setRemarks("");
        executeVerify(client, req);
    }

    private static void testVerifyWithFacial(NhifApiClient client, String cardNo) {
        System.out.println("--- B3: VerifyCard biometricMethod=FACIAL ---");
        String image = Base64.getEncoder().encodeToString("SAMPLE_FACIAL_IMAGE".getBytes());
        VerifyCardRequest req = new VerifyCardRequest();
        req.setCardNo(cardNo);
        req.setVerifierID(VERIFIER_ID);
        req.setCardTypeID(1);
        req.setBiometricMethod("FACIAL");
        req.setFpCode("");
        req.setImageData(image);
        req.setVisitTypeID(1);
        req.setReferralNo("");
        req.setRemarks("");
        executeVerify(client, req);
    }

    private static void executeVerify(NhifApiClient client, VerifyCardRequest request) {
        try {
            System.out.println("  REQUEST: " + MAPPER.writeValueAsString(request));
            CardVerification resp = client.verifyCard(request).get();
            System.out.println("  RESPONSE:");
            System.out.printf("    Card Number:  %s\n", resp.getCardNumber());
            System.out.printf("    Valid:        %s\n", resp.isValid());
            System.out.printf("    Status:       %s\n", resp.getStatus());
            System.out.printf("    Message:      %s\n", resp.getMessage());
            System.out.printf("    Member Name:  %s\n", resp.getMemberName());
            System.out.printf("    Member No:    %s\n", resp.getMemberNumber());
            System.out.printf("    Expiry Date:  %s\n", resp.getCardExpiryDate());
        } catch (Exception e) {
            System.out.println("  FAILED: " + extractMessage(e));
        }
        System.out.println();
    }

    private static String extractMessage(Exception e) {
        Throwable cause = e.getCause() != null ? e.getCause() : e;
        String msg = cause.getMessage();
        // Extract the JSON message if present
        int jsonStart = msg.indexOf("{");
        if (jsonStart >= 0) {
            return msg.substring(jsonStart);
        }
        return msg;
    }
}
