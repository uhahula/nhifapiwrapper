package com.oau.nhif.example;

import com.oau.nhif.client.NhifApiClient;
import com.oau.nhif.client.NhifApiClientFactory;
import com.oau.nhif.client.model.*;
import com.oau.nhif.exception.NhifApiException;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Test class for existing API endpoints to verify they work correctly
 */
public class TestExistingEndpoints {
    
    public static void main(String[] args) {
        // Initialize the client with your credentials
        String authBaseUrl = "https://verification.nhif.or.tz";
        String serviceBaseUrl = "http://test.nhif.or.tz/servicehub";
        String clientId = "11014";
        String clientSecret = "ntbzRGbrwwHj8Jwd7bbPsg==";
        String username = "Mtundi";
        
        try (NhifApiClient client = NhifApiClientFactory.createClient(
                authBaseUrl, 
                serviceBaseUrl,
                clientId, 
                clientSecret, 
                username)) {
            
            String cardNumber = "203801301050";
            
            // === VERIFICATION APIS ===
            System.out.println("=== TESTING VERIFICATION APIS ===");
            
            // Test 1: GetCardDetails (already working)
            System.out.println("\n1. Testing getCardDetails...");
            try {
                client.getCardDetails(cardNumber)
                    .thenAccept(card -> {
                        System.out.println("✓ Card Details: " + card.getFullName() + " - " + card.getCardStatus());
                        System.out.println("  Latest Authorization: " + card.getLatestAuthorization());
                    })
                    .get();
            } catch (Exception e) {
                System.err.println("✗ Error getting card details: " + e.getMessage());
            }
            
            // Test 2: GetPatientDetails
            System.out.println("\n2. Testing getPatientDetails...");
            try {
                client.getPatientDetails(cardNumber)
                    .thenAccept(patient -> {
                        System.out.println("✓ Patient: " + patient.getFullName());
                        System.out.println("  DOB: " + patient.getDateOfBirth());
                        System.out.println("  Gender: " + patient.getGender());
                    })
                    .get();
            } catch (Exception e) {
                System.err.println("✗ Error getting patient details: " + e.getMessage());
            }
            
            // Test 3: VerifyCard (need card number as string)
            System.out.println("\n3. Testing verifyCard...");
            try {
                client.verifyCard(cardNumber)
                    .thenAccept(verification -> {
                        System.out.println("✓ Card Verification successful: " + verification.toString());
                    })
                    .get();
            } catch (Exception e) {
                System.err.println("✗ Error verifying card: " + e.getMessage());
                if (e.getCause() != null) {
                    System.err.println("  Cause: " + e.getCause().getMessage());
                }
            }
            
            // === HISTORY APIS ===
            System.out.println("\n=== TESTING HISTORY APIS ===");
            
            // Test 4: GetPreviousPatientVisits
            System.out.println("\n4. Testing getPreviousPatientVisits...");
            try {
                client.getPreviousPatientVisits(cardNumber)
                    .thenAccept(visits -> {
                        System.out.println("✓ Found " + visits.size() + " previous visits");
                        visits.stream().limit(3).forEach(visit -> 
                            System.out.println("  - " + visit.getVisitDate() + " at " + visit.getFacilityName()));
                    })
                    .get();
            } catch (Exception e) {
                System.err.println("✗ Error getting previous visits: " + e.getMessage());
            }
            
            // Test 5: GetMedicalHistory
            System.out.println("\n5. Testing getMedicalHistory...");
            try {
                client.getMedicalHistory(cardNumber)
                    .thenAccept(history -> {
                        System.out.println("✓ Medical History: " + history.toString());
                    })
                    .get();
            } catch (Exception e) {
                System.err.println("✗ Error getting medical history: " + e.getMessage());
            }
            
            // === CLAIM APIS ===
            System.out.println("\n=== TESTING CLAIM APIS ===");
            
            // Test 6: Test Claim
            System.out.println("\n6. Testing testClaim...");
            try {
                client.testClaim()
                    .thenAccept(test -> {
                        System.out.println("✓ Test Claim: " + test.toString());
                    })
                    .get();
            } catch (Exception e) {
                System.err.println("✗ Error testing claim: " + e.getMessage());
            }
            
            // Test 7: Send Confirmation Code
            System.out.println("\n7. Testing sendConfirmationCode...");
            try {
                String phoneNumber = "+255700000000"; // Test phone number
                client.sendConfirmationCode(phoneNumber)
                    .thenAccept(confirmation -> {
                        System.out.println("✓ Confirmation Code: " + confirmation.toString());
                    })
                    .get();
            } catch (Exception e) {
                System.err.println("✗ Error sending confirmation code: " + e.getMessage());
            }
            
            System.out.println("\n=== TEST COMPLETED ===");
            
        } catch (NhifApiException e) {
            System.err.println("Client Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}