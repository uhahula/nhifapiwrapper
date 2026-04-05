package com.oau.nhif.example;

import com.oau.nhif.client.NhifApiClient;
import com.oau.nhif.client.NhifApiClientFactory;
import com.oau.nhif.client.model.*;
import com.oau.nhif.exception.NhifApiException;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Example class demonstrating how to use the NHIF Claims API endpoints
 */
public class ClaimsApiExample {
    
    public static void main(String[] args) {
        // Initialize the client with your credentials
        String authBaseUrl = "https://test.verification.nhif.or.tz";
        String serviceBaseUrl = "https://test.nhif.or.tz/ocs";
        String clientId = "11014";

        String clientSecret = "ntbzRGbrwwHj8Jwd7bbPsg==";
        String username = "Mtundi";

        try (NhifApiClient client = NhifApiClientFactory.createClient(
                authBaseUrl, serviceBaseUrl, clientId, clientSecret, username)) {

            System.out.println("=== NHIF Claims API Examples ===\n");

            // Example 1: Test Claims API
            //testClaimsApi(client);

            // Example 2: Get Submitted Claims
            getSubmittedClaimsExample(client);

            // Example 3: Submit Folio
            //submitFolioExample(client);

            // Example 4: Submit Monthly Claim
           // submitMonthlyClaimExample(client);

            // Example 5: Get Receipt
            getReceiptExample(client);

        } catch (NhifApiException | InterruptedException | ExecutionException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Example 1: Test Claims API connectivity
     */
    private static void testClaimsApi(NhifApiClient client) throws NhifApiException, InterruptedException, ExecutionException {
        System.out.println("1. Testing Claims API connectivity...");
        System.out.println("Endpoint: GET /api/Claims/Test");
        
        client.testClaim()
                .thenAccept(testResponse -> {
                    System.out.println("✓ Claims API Status: " + testResponse.getStatus());
                    System.out.println("✓ Claims API is accessible and running\n");
                })
                .exceptionally(throwable -> {
                    System.err.println("✗ Claims API test failed: " + throwable.getMessage());
                    return null;
                })
                .get();
    }

    /**
     * Example 2: Get submitted claims for a facility
     */
    private static void getSubmittedClaimsExample(NhifApiClient client) throws NhifApiException, InterruptedException, ExecutionException {
        System.out.println("2. Getting submitted claims...");
        System.out.println("Endpoint: GET /api/Claims/GetSubmittedClaims");
        
        String facilityCode = "11014";
        int claimYear = 2025;
        int claimMonth = 6;
        
        System.out.println("Parameters: facilityCode=" + facilityCode + ", claimYear=" + claimYear + ", claimMonth=" + claimMonth);
        
        client.getSubmittedClaims(facilityCode, claimYear, claimMonth)
                .thenAccept(claims -> {
                    System.out.println("✓ Found " + claims.size() + " submitted claim(s):");
                    
                    for (ClaimSubmission claim : claims) {
                        System.out.println("\n--- Claim Details ---");
                        System.out.println("Submission ID: " + claim.getSubmissionID());
                        System.out.println("Submission No: " + claim.getSubmissionNo());
                        System.out.println("Date Submitted: " + claim.getDateSubmitted());
                        System.out.println("Facility Code: " + claim.getFacilityCode());
                        System.out.println("Claim Year/Month: " + claim.getClaimYear() + "/" + claim.getClaimMonth());
                        System.out.println("Folio No: " + claim.getFolioNo());
                        System.out.println("Bill No: " + claim.getBillNo());
                        System.out.println("Submitted By: " + claim.getSubmittedBy());
                        System.out.println("Card No: " + claim.getCardNo());
                        System.out.println("Authorization No: " + claim.getAuthorizationNo());
                        System.out.println("Visit Type ID: " + claim.getVisitTypeID());
                        System.out.println("Scheme ID: " + claim.getSchemeID());
                        System.out.println("Amount Claimed: " + claim.getAmountClaimed());
                        System.out.println("Remarks: " + claim.getRemarks());
                        System.out.println("Submission Channel: " + claim.getSubmissionChannel());
                        System.out.println("Hash Code: " + claim.getHashCode());
                    }
                    System.out.println();
                })
                .exceptionally(throwable -> {
                    System.err.println("✗ Failed to get submitted claims: " + throwable.getMessage());
                    return null;
                })
                .get();
    }

    /**
     * Example 3: Submit a folio (individual claim)
     */
    private static void submitFolioExample(NhifApiClient client) throws NhifApiException, InterruptedException, ExecutionException {
        System.out.println("3. Submitting a folio (individual claim)...");
        System.out.println("Endpoint: POST /api/Claims/SubmitFolio");
        
        // Create a complete folio submission request
        FolioSubmissionRequest folioRequest = createSampleFolioRequest();
        
        System.out.println("Folio submission request:");
        System.out.println("Facility Code: " + folioRequest.getFacilityCode());
        System.out.println("Card No: " + folioRequest.getCardNo());
        System.out.println("Patient: " + folioRequest.getFirstName() + " " + folioRequest.getLastName());
        System.out.println("Authorization No: " + folioRequest.getAuthorizationNo());
        System.out.println("Bill No: " + folioRequest.getBillNo());
        System.out.println("Amount Claimed: " + folioRequest.getAmountClaimed());
        System.out.println("Items: " + (folioRequest.getFolioItems() != null ? folioRequest.getFolioItems().size() : 0));
        System.out.println("Diseases: " + (folioRequest.getFolioDiseases() != null ? folioRequest.getFolioDiseases().size() : 0));
        
        client.submitFolio(folioRequest)
                .thenAccept(response -> {
                    if (response.isSuccess()) {
                        System.out.println("✓ Folio submitted successfully!");
                        System.out.println("Submission ID: " + response.getSubmissionId());
                        System.out.println("Acknowledgement No: " + response.getAcknowledgementNo());
                        System.out.println("Date Submitted: " + response.getDateSubmitted());
                    } else {
                        System.out.println("✗ Folio submission failed:");
                        System.out.println("Status Code: " + response.getStatusCode());
                        System.out.println("Message: " + response.getMessage());
                        System.out.println("Reason: " + response.getReasonPhrase());
                    }
                })
                .exceptionally(throwable -> {
                    System.err.println("✗ Failed to submit folio: " + throwable.getMessage());
                    return null;
                })
                .get();
        
        System.out.println();
    }
    
    /**
     * Creates a sample folio submission request with all required fields
     */
    private static FolioSubmissionRequest createSampleFolioRequest() {
        FolioSubmissionRequest request = new FolioSubmissionRequest();
        
        // Basic claim information
        request.setFacilityCode("11014");
        request.setClaimYear(2025);
        request.setClaimMonth(8);
        request.setFolioNo(1);
        request.setBillNo("BILL001");
        request.setAmountClaimed(100.0);
        
        // Patient information
        request.setCardNo("101502314766");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setGender("Male");
        request.setDateOfBirth("1990-01-01T00:00:00.000Z");
        request.setTelephoneNo("0712345678");
        request.setPatientFileNo("FILE001");
        
        // Authorization and attendance
        request.setAuthorizationNo("180527859390");
        request.setAttendanceDate("2025-08-14T08:00:00.000Z");
        request.setPatientTypeCode("OUTPATIENT");
        
        // Clinical information
        request.setClinicalNotes("Patient consultation for general checkup");
        
        // Administrative info
        request.setCreatedBy("Mtundi");
        request.setDateCreated("2025-08-14T08:00:00.000Z");
        request.setLastModified("2025-08-14T08:00:00.000Z");
        request.setLastModifiedBy("Mtundi");
        
        // Add sample folio items
        java.util.List<FolioItem> items = new java.util.ArrayList<>();
        FolioItem item = new FolioItem();
        item.setItemCode("CONS001");
        item.setItemName("General Consultation");
        item.setItemTypeID(1);
        item.setUnitPrice(50.0);
        item.setItemQuantity(1);
        item.setAmountClaimed(50.0);
        item.setCreatedBy("Mtundi");
        item.setDateCreated("2025-08-14T08:00:00.000Z");
        items.add(item);
        
        FolioItem item2 = new FolioItem();
        item2.setItemCode("LAB001");
        item2.setItemName("Blood Test");
        item2.setItemTypeID(2);
        item2.setUnitPrice(50.0);
        item2.setItemQuantity(1);
        item2.setAmountClaimed(50.0);
        item2.setCreatedBy("Mtundi");
        item2.setDateCreated("2025-08-14T08:00:00.000Z");
        items.add(item2);
        
        request.setFolioItems(items);
        
        // Add sample diseases
        java.util.List<FolioDisease> diseases = new java.util.ArrayList<>();
        FolioDisease disease = new FolioDisease();
        disease.setDiseaseCode("Z000");
        disease.setStatus("Primary");
        disease.setRemarks("General health examination");
        disease.setCreatedBy("Mtundi");
        disease.setDateCreated("2025-08-14T08:00:00.000Z");
        diseases.add(disease);
        request.setFolioDiseases(diseases);
        
        return request;
    }

    /**
     * Example 4: Submit monthly claim
     */
    private static void submitMonthlyClaimExample(NhifApiClient client) throws NhifApiException, InterruptedException, ExecutionException {
        System.out.println("4. Submitting monthly claim...");
        System.out.println("Endpoint: POST /api/Claims/SubmitMonthlyClaim");
        
        // Create a complete monthly claim submission matching the API request format
        MonthlyClaimSubmission monthlyClaimRequest = new MonthlyClaimSubmission();
        monthlyClaimRequest.setFacilityCode("11014");
        monthlyClaimRequest.setClaimYear(2025);
        monthlyClaimRequest.setClaimMonth(8);
        monthlyClaimRequest.setFoliosSubmitted(1);
        monthlyClaimRequest.setTotalAmountClaimed(1.0);
        monthlyClaimRequest.setSubmissionRemarks("August claims");
        
        System.out.println("Monthly claim request:");
        System.out.println("Facility Code: " + monthlyClaimRequest.getFacilityCode());
        System.out.println("Claim Period: " + monthlyClaimRequest.getClaimYear() + "/" + monthlyClaimRequest.getClaimMonth());
        System.out.println("Folios Submitted: " + monthlyClaimRequest.getFoliosSubmitted());
        System.out.println("Total Amount Claimed: " + monthlyClaimRequest.getTotalAmountClaimed());
        System.out.println("Submission Remarks: " + monthlyClaimRequest.getSubmissionRemarks());
        
        client.submitMonthlyClaimSubmission(monthlyClaimRequest)
                .thenAccept(response -> {
                    if (response.isSuccess()) {
                        System.out.println("✓ Monthly claim submitted successfully!");
                        System.out.println("Acknowledgement No: " + response.getAcknowledgementNo());
                        System.out.println("Date Submitted: " + response.getDateSubmitted());
                        System.out.println("Submitted By: " + response.getSubmittedBy());
                        System.out.println("Response: " + response.toString());
                    } else {
                        System.out.println("✗ Monthly claim submission failed:");
                        System.out.println("Error Message: " + response.getErrorMessage());
                        System.out.println("Status Code: " + response.getStatusCode());
                    }
                })
                .exceptionally(throwable -> {
                    System.err.println("✗ Failed to submit monthly claim: " + throwable.getMessage());
                    return null;
                })
                .get();
        
        System.out.println();
    }

    /**
     * Example 5: Get receipt for a specific claim
     */
    private static void getReceiptExample(NhifApiClient client) throws NhifApiException, InterruptedException, ExecutionException {
        System.out.println("5. Getting receipt for a claim...");
        System.out.println("Endpoint: GET /api/Claims/GetReceipt");
        
        String facilityCode = "11014";
        int claimYear = 2025;
        int claimMonth = 6;
        String folioNo = "09";
        
        System.out.println("Parameters: facilityCode=" + facilityCode + 
                          ", claimYear=" + claimYear + 
                          ", claimMonth=" + claimMonth + 
                          ", folioNo=" + folioNo);
        
        client.getReceipt(facilityCode, claimYear, claimMonth, folioNo)
                .thenAccept(receipt -> {
                    System.out.println("✓ Receipt retrieved successfully:");
                    System.out.println("\n--- Receipt Details ---");
                    System.out.println("Receipt No: " + receipt.getReceiptNo());
                    System.out.println("Facility: " + receipt.getFacilityName() + " (" + receipt.getFacilityCode() + ")");
                    System.out.println("Claim Period: " + receipt.getClaimYear() + "/" + receipt.getClaimMonth());
                    System.out.println("Patient: " + receipt.getFirstName() + " " + receipt.getLastName());
                    System.out.println("Gender: " + receipt.getGender());
                    System.out.println("Card No: " + receipt.getCardNo());
                    System.out.println("Telephone: " + receipt.getTelephoneNo());
                    System.out.println("Patient File No: " + receipt.getPatientFileNo());
                    System.out.println("Authorization No: " + receipt.getAuthorizationNo());
                    System.out.println("Total Amount Claimed: " + receipt.getAmountClaimed());
                    
                    if (receipt.getReceiptItems() != null && !receipt.getReceiptItems().isEmpty()) {
                        System.out.println("\n--- Receipt Items ---");
                        for (GetReceiptResponse.ReceiptItem item : receipt.getReceiptItems()) {
                            System.out.println("Item Code: " + item.getItemCode());
                            System.out.println("Item Name: " + item.getItemName());
                            System.out.println("Unit Price: " + item.getUnitPrice());
                            System.out.println("Quantity: " + item.getItemQuantity());
                            System.out.println("Amount Claimed: " + item.getAmountClaimed());
                            if (item.getOtherDetails() != null) {
                                System.out.println("Other Details: " + item.getOtherDetails());
                            }
                            System.out.println("---");
                        }
                    }
                })
                .exceptionally(throwable -> {
                    System.err.println("✗ Failed to get receipt: " + throwable.getMessage());
                    return null;
                })
                .get();
        
        System.out.println();
    }
}