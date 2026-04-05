package com.oau.nhif.example;

import com.oau.nhif.client.NhifApiClient;
import com.oau.nhif.client.NhifApiClientFactory;
import com.oau.nhif.client.model.CardAuthorizationRequest;
import com.oau.nhif.client.model.CardAuthorizationResponse;
import com.oau.nhif.client.model.SimpleCardAuthorizationRequest;
import com.oau.nhif.exception.NhifApiException;

import java.util.concurrent.ExecutionException;

/**
 * Example demonstrating card authorization with biometric verification
 */
public class CardAuthorizationExample {
    
    public static void main(String[] args) {
        // Initialize the client with your credentials
        String authBaseUrl = "https://test.nhif.or.tz";
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
            
            // Example 1: Simple card authorization (no biometric required)
            authorizeCardSimple(client);
            
        } catch (NhifApiException | InterruptedException | ExecutionException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void authorizeCardSimple(NhifApiClient client) throws InterruptedException, ExecutionException {
        System.out.println("=== Simple Card Authorization (No Biometric) ===");
        
        // Method 1: Using convenience method
        String cardNumber = "101502314766";
        try {
            client.authorizeCardSimple(cardNumber, 1)
                    .thenAccept(response -> {
                        System.out.println("Authorization Status: " + response.getAuthorizationStatus());
                        System.out.println("Authorization ID: " + response.getAuthorizationID());
                        System.out.println("Card Status: " + response.getCardStatus());
                        System.out.println("Is Valid Card: " + response.getIsValidCard());
                        System.out.println("Is Active: " + response.getIsActive());

                        if (response.getFullName() != null) {
                            System.out.println("Patient: " + response.getFullName() + 
                                    " (Member: " + response.getMembershipNo() + ")");
                        }
                        
                        if (response.getRemarks() != null) {
                            System.out.println("Remarks: " + response.getRemarks());
                        }
                    })
                    .get(); // Blocking call for demo purposes

        } catch (NhifApiException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        // Method 2: Using request object for more control
        System.out.println("\nUsing request object for detailed authorization...");
        SimpleCardAuthorizationRequest request = new SimpleCardAuthorizationRequest(
            cardNumber,             // cardNo
            1,                      // visitTypeID (Normal visit)
            "",        // referralNo
                "12321321", // nationalID
            "Quick consultation"    // remarks
        );

        try {
            client.authorizeCardSimple(request)
                    .thenAccept(response -> {
                        System.out.println("Authorization ID: " + response.getAuthorizationID());
                        System.out.println("Authorization Status: " + response.getAuthorizationStatus());
                        System.out.println("Card No: " + response.getCardNo());
                        System.out.println("Full Name: " + response.getFullName());
                        System.out.println("Gender: " + response.getGender());
                        System.out.println("Date of Birth: " + response.getDateOfBirth());
                        System.out.println("Scheme Name: " + response.getSchemeName());
                        System.out.println("Expiry Date: " + response.getExpiryDate());
                        
                        if ("REJECTED".equals(response.getAuthorizationStatus())) {
                            System.out.println("⚠️ Authorization rejected: " + response.getStatusDescription());
                        } else if ("APPROVED".equals(response.getAuthorizationStatus())) {
                            System.out.println("✅ Authorization approved");
                            if (response.getAuthorizationNo() != null) {
                                System.out.println("Authorization No: " + response.getAuthorizationNo());
                            }
                        }
                    })
                    .get();
        } catch (NhifApiException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}