package com.oau.nhif.example;

import com.oau.nhif.client.NhifApiClient;
import com.oau.nhif.client.NhifApiClientFactory;
import com.oau.nhif.client.model.*;
import com.oau.nhif.exception.NhifApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Example class demonstrating how to use the NHIF API client
 */
public class NhifApiExample {
    
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
            
           /* // Example 1: Get card details
            String cardNumber = "203801301050";
            System.out.println("Fetching card details for: " + cardNumber);
            client.getCardDetails(cardNumber)
                .thenAccept(card -> {
                    System.out.println("Card Holder: " + card.getFullName());
                    System.out.println("Status: " + card.getCardStatus());
                    System.out.println("Status Description: " + card.getStatusDescription());
                    System.out.println("Expiry: " + card.getExpiryDate());
                    System.out.println("Is Active: " + card.getIsActive());
                    System.out.println("Is Valid Card: " + card.getIsValidCard());
                    System.out.println("Latest Authorization: " + card.getLatestAuthorization());
                })
                .get(); // Blocking call for demo purposes
            
            // Example 2: Verify card
             System.out.println("\nVerifying card: " + cardNumber);
            client.verifyCard(cardNumber)
                .thenAccept(verification -> {
                    System.out.println("Card is " + (verification.isValid() ? "valid" : "invalid"));
                    System.out.println("Member: " + verification.getMemberName());
                })
                .get();
            
            // Example 3: Get patient details
            System.out.println("\nFetching patient details...");
            client.getPatientDetails(cardNumber)
                .thenAccept(patient -> {
                    System.out.println("Patient: " + patient.getFullName());
                    System.out.println("Date of Birth: " + patient.getDateOfBirth());
                    System.out.println("Dependents: " + patient.getDependents().size());
                })
                .get();
            
            // Example 4: Get patient visit history
            System.out.println("\nFetching visit history...");
            client.getPreviousPatientVisits(cardNumber)
                .thenAccept(visits -> {
                    System.out.println("Found " + visits.size() + " visits");
                    if (!visits.isEmpty()) {
                        System.out.println("Most recent visit: " + visits.get(0).getVisitDate() + 
                                         " at " + visits.get(0).getFacilityName());
                    }
                })
                .get();
            
            // Example 5: Test claim submission
            System.out.println("\nTesting claim submission...");
            client.testClaim()
                .thenAccept(test -> {
                    System.out.println("Test result: " + test.getMessage());
                })
                .get();

            // Example 6: Authorize Card
            System.out.println("\nAuthorizing card: " + cardNumber);
            AuthorizationRequest authRequest = new AuthorizationRequest();
            authRequest.setCardNumber(cardNumber);
            authRequest.setDiagnosis("Test Diagnosis");
            authRequest.setFacilityCode(clientId);
            authRequest.setVisitDate("2023-05-01");
            authRequest.setReferralNumber("OP");
            authRequest.setRemarks("Test Remarks");

            List<AuthorizationRequest.AuthorizationItem> authItems = new ArrayList<>();
            AuthorizationRequest.AuthorizationItem item1 = new AuthorizationRequest.AuthorizationItem();
            item1.setItemCode("ItemCode1");
            item1.setUnitPrice(1000);
            item1.setQuantity(1);
            authItems.add(item1);

            authRequest.setItems(authItems);

            client.authorizeCard(authRequest)
                .thenAccept(authorization -> {
                    System.out.println("Authorization ID: " + authorization.getAuthorizationNumber());
                    System.out.println("Authorization result: " + authorization.getMessage());
                })
                .get();

*/
            // Example 7: Get authorization details

            String autho = "480527859458";
            // GenericResponse admtype9 = client.getDetailsByAuthorizationNo(admin).get();
            System.out.println("\nGetAuthorization Details:");
            client.getAuthorizationDetails(autho)
                    .thenAccept(card4 -> {
                        System.out.println("Authorization Number: " + card4.getAuthorizationNo());
                        System.out.println("Authorization ID: " + card4.getAuthorizationId());
                        System.out.println("Card Number: " + card4.getCardNo());
                        System.out.println("Member Number: " + card4.getMembershipNo());
                        System.out.println("Full Name: " + card4.getFullName());
                        System.out.println("Authorization Status: " + card4.getAuthorizationStatus());
                        System.out.println("Authorization Date: " + card4.getAuthorizationDate());
                        System.out.println("Facility Code: " + card4.getFacilityCode());
                        System.out.println("Card Status: " + card4.getCardStatus());
                        System.out.println("Is Valid Card: " + card4.getIsValidCard());

                    })
                    .get();



        } catch (NhifApiException | InterruptedException | ExecutionException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }


    }
}
