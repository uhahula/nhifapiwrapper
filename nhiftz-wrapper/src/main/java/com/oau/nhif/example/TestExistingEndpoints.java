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
        String authBaseUrl = "https://test.verification.nhif.or.tz";
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
            
            // === REFERENCE DATA APIS ===
            System.out.println("\n=== TESTING REFERENCE DATA APIS ===");
            
            // Test 8: Get Points of Care
            System.out.println("\n8. Testing getPointsOfCare...");
            try {
                client.getPointsOfCare()
                    .thenAccept(pocs -> {
                        System.out.println("✓ Points of Care: " + pocs.size() + " items");
                        pocs.stream().limit(3).forEach(poc -> 
                            System.out.println("  - " + poc.toString()));
                    })
                    .get();
            } catch (Exception e) {
                System.err.println("✗ Error getting points of care: " + e.getMessage());
            }
            
            // Test 9: Get Visit Types
            System.out.println("\n9. Testing getVisitTypes...");
            try {
                client.getVisitTypes()
                    .thenAccept(types -> {
                        System.out.println("✓ Visit Types: " + types.size() + " items");
                        types.stream().limit(3).forEach(type -> 
                            System.out.println("  - " + type.toString()));
                    })
                    .get();
            } catch (Exception e) {
                System.err.println("✗ Error getting visit types: " + e.getMessage());
            }
            
            // Test 10: Get Facilities
            System.out.println("\n10. Testing getFacilities...");
            try {
                client.getFacilities()
                    .thenAccept(facilities -> {
                        System.out.println("✓ Facilities: " + facilities.size() + " items");
                        facilities.stream().limit(3).forEach(facility -> 
                            System.out.println("  - " + facility.toString()));
                    })
                    .get();
            } catch (Exception e) {
                System.err.println("✗ Error getting facilities: " + e.getMessage());
            }
            
            // === ADMISSIONS APIS ===
            System.out.println("\n=== TESTING ADMISSIONS APIS ===");
            
            // Test 11: Get Admission Types
            System.out.println("\n11. Testing getAdmissionTypes...");
            try {
                client.getAdmissionTypes()
                    .thenAccept(types -> {
                        System.out.println("✓ Admission Types: " + types.size() + " items");
                        types.stream().limit(3).forEach(type -> 
                            System.out.println("  - ID: " + type.getAdmissionTypeID() + 
                                             ", Name: " + type.getAdmissionTypeName()));
                    })
                    .get();
            } catch (Exception e) {
                System.err.println("✗ Error getting admission types: " + e.getMessage());
            }
            
            // Test 12: Get Ward Types
            System.out.println("\n12. Testing getWardTypes...");
            try {
                client.getWardTypes()
                    .thenAccept(types -> {
                        System.out.println("✓ Ward Types: " + types.size() + " items");
                        types.stream().limit(3).forEach(type -> 
                            System.out.println("  - ID: " + type.getWardTypeID() + 
                                             ", Name: " + type.getWardTypeName()));
                    })
                    .get();
            } catch (Exception e) {
                System.err.println("✗ Error getting ward types: " + e.getMessage());
            }
            
            // Test 13: Get Room Types
            System.out.println("\n13. Testing getRoomTypes...");
            try {
                client.getRoomTypes()
                    .thenAccept(types -> {
                        System.out.println("✓ Room Types: " + types.size() + " items");
                        types.stream().limit(3).forEach(type -> 
                            System.out.println("  - ID: " + type.getRoomTypeID() + 
                                             ", Name: " + type.getRoomTypeName()));
                    })
                    .get();
            } catch (Exception e) {
                System.err.println("✗ Error getting room types: " + e.getMessage());
            }
            
            // Test 14: Get Discharge Types
            System.out.println("\n14. Testing getDischargeTypes...");
            try {
                client.getDischargeTypes()
                    .thenAccept(types -> {
                        System.out.println("✓ Discharge Types: " + types.size() + " items");
                        types.stream().limit(3).forEach(type -> 
                            System.out.println("  - ID: " + type.getDischargeTypeID() + 
                                             ", Name: " + type.getDischargeTypeName()));
                    })
                    .get();
            } catch (Exception e) {
                System.err.println("✗ Error getting discharge types: " + e.getMessage());
            }
            
            // Test 15: Get Admitted Patients
            System.out.println("\n15. Testing getAdmittedPatients...");
            try {
                client.getAdmittedPatients()
                    .thenAccept(patients -> {
                        System.out.println("✓ Admitted Patients: " + patients.size() + " items");
                        patients.stream().limit(3).forEach(patient -> 
                            System.out.println("  - " + patient.getFullName() + 
                                             " (Admission: " + patient.getAdmissionNo() + ")"));
                    })
                    .get();
            } catch (Exception e) {
                System.err.println("✗ Error getting admitted patients: " + e.getMessage());
            }
            
            System.out.println("\n=== ALL TESTS COMPLETED ===");
            
        } catch (NhifApiException e) {
            System.err.println("Client Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}