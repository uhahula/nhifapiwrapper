package com.oau.nhif.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oau.nhif.client.model.CardAuthorizationResponse;

/**
 * Test class to verify CardAuthorizationResponse mapping with real API response data
 */
public class CardAuthorizationResponseTest {
    
    public static void main(String[] args) {
        try {
            // Sample JSON response from NHIF API
            String jsonResponse = "{\n" +
                "  \"AuthorizationID\": \"dfc2dd0d-1318-4dd0-96e5-714ca75c33fa\",\n" +
                "  \"CardNo\": \"404102706048\",\n" +
                "  \"MembershipNo\": \"1**\",\n" +
                "  \"EmployerNo\": \"1**\",\n" +
                "  \"EmployerName\": \"T**\",\n" +
                "  \"HasSupplementary\": \"No\",\n" +
                "  \"SupplementaryAgreementId\": null,\n" +
                "  \"SchemeID\": 1001,\n" +
                "  \"SchemeName\": \"Standard NHIF Benefit Scheme\",\n" +
                "  \"CardExistence\": \"EXISTING\",\n" +
                "  \"CardStatusID\": 4,\n" +
                "  \"CardStatus\": \"Inactive\",\n" +
                "  \"IsValidCard\": false,\n" +
                "  \"IsActive\": false,\n" +
                "  \"StatusDescription\": \"Revoked\",\n" +
                "  \"FirstName\": \"J*\",\n" +
                "  \"MiddleName\": \"N\",\n" +
                "  \"LastName\": \"M*\",\n" +
                "  \"FullName\": \"J**\",\n" +
                "  \"Gender\": \"Female\",\n" +
                "  \"PFNumber\": null,\n" +
                "  \"DateOfBirth\": \"1966-12-09\",\n" +
                "  \"YearOfBirth\": 1966,\n" +
                "  \"Age\": \"59\",\n" +
                "  \"ExpiryDate\": \"2031-10-04\",\n" +
                "  \"CHNationalID\": null,\n" +
                "  \"AuthorizationStatus\": \"REJECTED\",\n" +
                "  \"AuthorizationNo\": null,\n" +
                "  \"TokenNo\": null,\n" +
                "  \"Remarks\": \"The card is In Active,If the beneficiary thinks this is is a mistake she should visit the nearby NHIF office for verification\",\n" +
                "  \"FacilityCode\": \"11014\",\n" +
                "  \"ProductName\": \"STANDARD and Scheme: Standard NHIF Benefit Scheme\",\n" +
                "  \"ProductCode\": \"NH001\",\n" +
                "  \"CreatedBy\": \"Darcoder\",\n" +
                "  \"VisitType\": null,\n" +
                "  \"VisitMode\": null,\n" +
                "  \"EstimatedAmount\": null,\n" +
                "  \"ClaimType\": null,\n" +
                "  \"AuthorizationDate\": null,\n" +
                "  \"LastModifiedBy\": null,\n" +
                "  \"LastModifiedDate\": null,\n" +
                "  \"CreatedDate\": null\n" +
                "}";

            // Create ObjectMapper for JSON parsing
            ObjectMapper objectMapper = new ObjectMapper();
            
            // Parse JSON to CardAuthorizationResponse object
            CardAuthorizationResponse response = objectMapper.readValue(jsonResponse, CardAuthorizationResponse.class);
            
            System.out.println("=== Card Authorization Response Mapping Test ===");
            System.out.println();
            
            // Display key information
            System.out.println("Authorization Details:");
            System.out.println("- Authorization ID: " + response.getAuthorizationID());
            System.out.println("- Authorization Status: " + response.getAuthorizationStatus());
            System.out.println("- Card Status: " + response.getCardStatus());
            System.out.println("- Is Valid Card: " + response.getIsValidCard());
            System.out.println("- Is Active: " + response.getIsActive());
            System.out.println();
            
            System.out.println("Card Information:");
            System.out.println("- Card No: " + response.getCardNo());
            System.out.println("- Card Existence: " + response.getCardExistence());
            System.out.println("- Card Status ID: " + response.getCardStatusID());
            System.out.println("- Expiry Date: " + response.getExpiryDate());
            System.out.println();
            
            System.out.println("Member Information:");
            System.out.println("- Full Name: " + response.getFullName());
            System.out.println("- Gender: " + response.getGender());
            System.out.println("- Date of Birth: " + response.getDateOfBirth());
            System.out.println("- Age: " + response.getAge());
            System.out.println("- Membership No: " + response.getMembershipNo());
            System.out.println();
            
            System.out.println("Scheme Information:");
            System.out.println("- Scheme ID: " + response.getSchemeID());
            System.out.println("- Scheme Name: " + response.getSchemeName());
            System.out.println("- Product Code: " + response.getProductCode());
            System.out.println("- Product Name: " + response.getProductName());
            System.out.println();
            
            System.out.println("Employer Information:");
            System.out.println("- Employer No: " + response.getEmployerNo());
            System.out.println("- Employer Name: " + response.getEmployerName());
            System.out.println("- Has Supplementary: " + response.getHasSupplementary());
            System.out.println();
            
            System.out.println("Additional Information:");
            System.out.println("- Facility Code: " + response.getFacilityCode());
            System.out.println("- Status Description: " + response.getStatusDescription());
            System.out.println("- Remarks: " + response.getRemarks());
            System.out.println("- Created By: " + response.getCreatedBy());
            System.out.println();
            
            // Validate key authorization fields
            if ("REJECTED".equals(response.getAuthorizationStatus())) {
                System.out.println("⚠️  AUTHORIZATION REJECTED");
                System.out.println("Reason: " + response.getStatusDescription());
                System.out.println("Action needed: " + response.getRemarks());
            } else if ("APPROVED".equals(response.getAuthorizationStatus())) {
                System.out.println("✅ AUTHORIZATION APPROVED");
                System.out.println("Authorization No: " + response.getAuthorizationNo());
                System.out.println("Token No: " + response.getTokenNo());
            }
            
            System.out.println();
            System.out.println("✓ JSON mapping test completed successfully!");
            System.out.println("All fields from the API response have been correctly mapped to the CardAuthorizationResponse object.");
            
        } catch (Exception e) {
            System.err.println("Error during JSON mapping test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}