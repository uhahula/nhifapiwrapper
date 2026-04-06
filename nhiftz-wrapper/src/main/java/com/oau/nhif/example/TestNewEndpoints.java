package com.oau.nhif.example;

import com.oau.nhif.client.NhifApiClient;
import com.oau.nhif.client.NhifApiClientFactory;
import com.oau.nhif.client.model.*;
import com.oau.nhif.exception.NhifApiException;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Test class for new API endpoints
 */
public class TestNewEndpoints {
    
    public static void main(String[] args) {
        // Initialize the client with your credentials
        String authBaseUrl = "https://test.nhif.or.tz";
        String serviceBaseUrl = "https://test.nhif.or.tz/servicehub";
        String clientId = "11014";
        String clientSecret = "ntbzRGbrwwHj8Jwd7bbPsg==";
        String username = "Mtundi";
        
        try (NhifApiClient client = NhifApiClientFactory.createClient(
                authBaseUrl, 
                serviceBaseUrl,
                clientId, 
                clientSecret, 
                username)) {
            
            // Test 1: Get Points of Care
            System.out.println("=== Testing getPointsOfCare ===");
            try {
                client.getPointsOfCare()
                    .thenAccept(pointsOfCare -> {
                        System.out.println("Found " + pointsOfCare.size() + " points of care");
                        pointsOfCare.stream().limit(5).forEach(poc -> 
                            System.out.println("- " + poc.getPointOfCareName() + " (" + poc.getPointOfCareCode() + ")"));
                    })
                    .get();
            } catch (Exception e) {
                System.err.println("Error getting points of care: " + e.getMessage());
            }
            
            // Test 2: Get Visit Types
            System.out.println("\n=== Testing getVisitTypes ===");
            try {
                client.getVisitTypes()
                    .thenAccept(visitTypes -> {
                        System.out.println("Found " + visitTypes.size() + " visit types");
                        visitTypes.stream().limit(5).forEach(vt -> 
                            System.out.println("- " + vt.getVisitTypeName() + " (ID: " + vt.getVisitTypeID() + ", Alias: " + vt.getAlias() + ")"));
                    })
                    .get();
            } catch (Exception e) {
                System.err.println("Error getting visit types: " + e.getMessage());
            }
            
            // Test 3: Get Facilities
            System.out.println("\n=== Testing getFacilities ===");
            try {
                client.getFacilities()
                    .thenAccept(facilities -> {
                        System.out.println("Found " + facilities.size() + " facilities");
                        facilities.stream().limit(5).forEach(f -> 
                            System.out.println("- " + f.getFacilityName() + " (" + f.getFacilityCode() + ", Level: " + f.getFacilityLevelCode() + ")"));
                    })
                    .get();
            } catch (Exception e) {
                System.err.println("Error getting facilities: " + e.getMessage());
            }
            
            // Test 4: Check Eligibility (using known card and a common service code)
            System.out.println("\n=== Testing checkEligibility ===");
            String cardNumber = "203801301050";
            String itemCode = "CONSULT"; // Common consultation service
            try {
                client.checkEligibility(cardNumber, itemCode)
                    .thenAccept(eligibility -> {
                        System.out.println("Eligibility for " + itemCode + ":");
                        System.out.println("- Can Access: " + eligibility.getCanAccess());
                        System.out.println("- Remarks: " + eligibility.getRemarks());
                        System.out.println("- (Legacy) Eligible: " + eligibility.getIsEligible());
                        System.out.println("- (Legacy) Message: " + eligibility.getEligibilityMessage());
                    })
                    .get();
            } catch (Exception e) {
                System.err.println("Error checking eligibility: " + e.getMessage());
            }
            
        } catch (NhifApiException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}