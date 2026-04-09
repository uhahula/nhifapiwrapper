package com.oau.nhif.example;

import com.oau.nhif.client.NhifApiClient;
import com.oau.nhif.client.NhifApiClientFactory;
import com.oau.nhif.client.model.CardAuthorizationRequest;
import com.oau.nhif.client.model.CardAuthorizationResponse;
import com.oau.nhif.client.model.GenericResponse;
import com.oau.nhif.client.model.PointOfCareReferenceRequest;

import com.oau.nhif.client.model.PractitionerAttendanceRequest;
import com.oau.nhif.exception.NhifApiException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.concurrent.ExecutionException;

/**
 * End-to-end example of the three NHIF biometric verification flows:
 *
 *   1. Practitioner attendance login (who is providing care)
 *   2. Card authorization with fingerprint (confirming the patient)
 *   3. Point-of-care reference generation (patient arriving at a station)
 *
 * The {@code imageData} field on all three requests is the base64-encoded
 * fingerprint template (or facial image) produced by your biometric capture
 * SDK. In a real integration you would capture it live from the device —
 * here we read it from a file for illustration.
 */
public class BiometricVerificationExample {

    // Biometric method constants as expected by the NHIF API
    private static final String METHOD_FINGERPRINT = "FINGERPRINT";
    private static final String METHOD_FACIAL = "FACIAL";

    public static void main(String[] args) {
        String authBaseUrl = "https://test.nhif.or.tz";
        String serviceBaseUrl = "https://test.nhif.or.tz/servicehub";
        String clientId = "11014";
        String clientSecret = "ntbzRGbrwwHj8Jwd7bbPsg==";
        String username = "Mtundi";

        try (NhifApiClient client = NhifApiClientFactory.createClient(
                authBaseUrl, serviceBaseUrl, clientId, clientSecret, username)) {

            // Capture the templates once per interaction. In production these
            // come from the fingerprint scanner SDK, not a file on disk.
            String practitionerTemplate = captureBiometric("practitioner.fpt");
            String patientTemplate = captureBiometric("patient.fpt");

            // 1. Practitioner logs in for their shift using a fingerprint
            String authorizationNo = loginPractitionerBiometric(client, practitionerTemplate);

            // 2. Patient card gets authorized via fingerprint match
            CardAuthorizationResponse auth =
                    authorizeCardBiometric(client, patientTemplate);

            // 3. Patient presents at a point of care (e.g. pharmacy); generate
            //    a POC reference using the practitioner's biometric.
            if (auth != null && "APPROVED".equals(auth.getAuthorizationStatus())) {
                generatePocReference(client, auth.getAuthorizationNo(),
                        "PRAC-00123", practitionerTemplate);
            }

        } catch (NhifApiException | InterruptedException | ExecutionException e) {
            System.err.println("Biometric flow failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------------------------
    // 1. Practitioner attendance login with biometric
    // ---------------------------------------------------------------------
    private static String loginPractitionerBiometric(NhifApiClient client,
                                                     String fingerprintTemplate)
            throws InterruptedException, ExecutionException {

        System.out.println("=== 1. Practitioner biometric login ===");

        PractitionerAttendanceRequest request = new PractitionerAttendanceRequest(
                "19850101-12345-67890-12", // nationalID
                "PRAC-00123",              // practitionerNo
                METHOD_FINGERPRINT,        // biometricMethod
                "R_INDEX",                 // fpCode — finger position
                fingerprintTemplate        // imageData — base64 template
        );

        try {
            GenericResponse response = client.loginPractitioner(request).get();
            System.out.println("Login response: " + response);
            return response != null ? response.toString() : null;
        } catch (NhifApiException e) {
            System.out.println("Practitioner login failed: " + e.getMessage());
            return null;
        }
    }

    // ---------------------------------------------------------------------
    // 2. Card authorization with biometric verification
    // ---------------------------------------------------------------------
    private static CardAuthorizationResponse authorizeCardBiometric(
            NhifApiClient client, String fingerprintTemplate)
            throws InterruptedException, ExecutionException {

        System.out.println("\n=== 2. Card authorization (biometric) ===");

        CardAuthorizationRequest request = new CardAuthorizationRequest()
                .cardNo("101502314766")
                .biometricMethod(METHOD_FINGERPRINT)
                .nationalID("19900101-12345-67890-12")
                .fpCode("R_THUMB")
                .imageData(fingerprintTemplate)
                .visitTypeID(1)               // Normal visit
                .referralNo("")
                .remarks("Biometric verified visit");

        try {
            CardAuthorizationResponse response =
                    client.authorizeCardWithBiometric(request).get();

            System.out.println("Status:          " + response.getAuthorizationStatus());
            System.out.println("Authorization #: " + response.getAuthorizationNo());
            System.out.println("Patient:         " + response.getFullName());
            System.out.println("Scheme:          " + response.getSchemeName());

            if ("REJECTED".equals(response.getAuthorizationStatus())) {
                System.out.println("Rejected: " + response.getStatusDescription());
            }
            return response;

        } catch (NhifApiException e) {
            System.out.println("Card authorization failed: " + e.getMessage());
            return null;
        }
    }

    // ---------------------------------------------------------------------
    // 3. Point-of-care reference generation with biometric
    // ---------------------------------------------------------------------
    private static void generatePocReference(NhifApiClient client,
                                             String authorizationNo,
                                             String practitionerNo,
                                             String fingerprintTemplate)
            throws InterruptedException, ExecutionException {

        System.out.println("\n=== 3. POC reference (biometric) ===");

        PointOfCareReferenceRequest request = new PointOfCareReferenceRequest(
                3,                        // pointOfCareID — e.g. Pharmacy
                authorizationNo,
                practitionerNo,
                METHOD_FINGERPRINT,
                "R_INDEX",
                fingerprintTemplate
        );

        try {
            GenericResponse response = client.generatePOCReferenceNo(request).get();
            System.out.println("POC response: " + response);
        } catch (NhifApiException e) {
            System.out.println("POC reference generation failed: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    /**
     * Stand-in for a real biometric SDK capture. Reads raw template bytes
     * from a file and base64-encodes them for transport. Replace this with
     * your scanner SDK's capture call in production.
     */
    private static String captureBiometric(String path) {
        try {
            byte[] bytes = Files.readAllBytes(Path.of(path));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            // Fall back to a placeholder so the example still runs end-to-end
            // against the test environment when no scanner is attached.
            return Base64.getEncoder().encodeToString("SAMPLE_TEMPLATE".getBytes());
        }
    }
}
