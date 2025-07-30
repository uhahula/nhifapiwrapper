package com.oau.nhif.example;

import com.oau.nhif.client.NhifApiClient;
import com.oau.nhif.client.NhifApiClientFactory;
import com.oau.nhif.client.model.*;
import com.oau.nhif.exception.NhifApiException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Example class demonstrating how to submit a folio claim using the NHIF Claims API
 * This covers the complete folio submission process including:
 * - Patient information and demographics
 * - Medical diagnoses (diseases)
 * - Claimed items and services
 * - Digital signatures
 * - Clinical notes and practitioner details
 */
public class FolioSubmissionExample {
    
    /**
     * Helper method to format date for .NET API compatibility
     * Format: "2025-07-30T07:15:41.435Z"
     */
    private static String formatDateForApi(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneOffset.UTC).toInstant().toString();
    }
    
    /**
     * Helper method to get current date formatted for API
     */
    private static String getCurrentDateForApi() {
        return Instant.now().toString();
    }
    
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
            
            System.out.println("=== NHIF Folio Submission Example ===\n");
            
            // Create a sample folio submission
            FolioSubmissionRequest folio = createSampleFolioSubmission(clientId);
            
            System.out.println("1. Submitting folio claim...");
            System.out.println("Patient: " + folio.getFirstName() + " " + folio.getLastName());
            System.out.println("Card Number: " + folio.getCardNo());
            System.out.println("Authorization: " + folio.getAuthorizationNo());
            System.out.printf("Amount Claimed: TSh %,.2f\n", folio.getAmountClaimed());
            System.out.println("Items: " + (folio.getFolioItems() != null ? folio.getFolioItems().size() : 0));
            System.out.println("Diseases: " + (folio.getFolioDiseases() != null ? folio.getFolioDiseases().size() : 0));
            
            // Submit the folio
            ClaimSubmissionResponse response = client.submitFolio(folio).get();
            System.out.println("\n2. Folio submission result:");
            System.out.println("Response: " + response.toString());
            
            System.out.println("\n=== Folio Submission Example Complete ===");
            
        } catch (NhifApiException | InterruptedException | ExecutionException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Creates a sample folio submission with realistic medical data
     */
    private static FolioSubmissionRequest createSampleFolioSubmission(String facilityCode) {
        FolioSubmissionRequest folio = new FolioSubmissionRequest();
        
        // Basic facility and claim information
        folio.setFacilityCode(facilityCode);
        folio.setClaimYear(2025);
        folio.setClaimMonth(7);
        folio.setFolioNo(1001);
        
        // Patient demographics
        folio.setCardNo("NHIF123456789");
        folio.setFirstName("John");
        folio.setLastName("Doe");
        folio.setGender("M");
        folio.setDateOfBirth(formatDateForApi(LocalDateTime.of(1985, 3, 15, 0, 0)));
        folio.setTelephoneNo("+255712345678");
        folio.setPatientFileNo("PAT001");
        folio.setBillNo("BILL2025001");
        
        // Visit and authorization details
        folio.setAuthorizationNo("AUTH2025001");
        folio.setAttendanceDate(getCurrentDateForApi());
        folio.setPatientTypeCode("OUTPATIENT");
        folio.setClinicalNotes("Patient presented with chest pain and difficulty breathing. " +
                              "Physical examination revealed elevated blood pressure and irregular heartbeat. " +
                              "ECG performed showing signs of cardiac stress. " +
                              "Prescribed medication and recommended follow-up visit.");
        
        // For outpatient, admission/discharge dates can be null or same as attendance
        folio.setDateAdmitted(null);
        folio.setDateDischarged(null);
        
        // Practitioners
        folio.setAttendingPractitioners(Arrays.asList("DR001", "NRS003"));
        
        // Claim details
        folio.setLateSubmissionReason(null); // On-time submission
        folio.setAmountClaimed(125000.0); // TSh 125,000
        folio.setConfirmationCode("CONF2025001");
        
        // Create folio diseases (diagnoses)
        folio.setFolioDiseases(createSampleDiseases());
        
        // Create folio items (services and supplies)
        folio.setFolioItems(createSampleFolioItems());
        
        // Create signatures
        folio.setSignatures(createSampleSignatures());
        
        // Audit fields
        folio.setDateCreated(getCurrentDateForApi());
        folio.setCreatedBy("SYSTEM");
        folio.setLastModified(getCurrentDateForApi());
        folio.setLastModifiedBy("SYSTEM");
        
        return folio;
    }
    
    /**
     * Creates sample diseases for the folio
     */
    private static List<FolioDisease> createSampleDiseases() {
        // Primary diagnosis: Hypertension
        FolioDisease primaryDisease = new FolioDisease();
        primaryDisease.setDiseaseCode("I10");
        primaryDisease.setStatus("PRIMARY");
        primaryDisease.setRemarks("Essential hypertension diagnosed during routine check-up");
        primaryDisease.setCreatedBy("DR001");
        primaryDisease.setDateCreated(getCurrentDateForApi());
        primaryDisease.setLastModified(getCurrentDateForApi());
        primaryDisease.setLastModifiedBy("DR001");
        
        // Secondary diagnosis: Chest pain
        FolioDisease secondaryDisease = new FolioDisease();
        secondaryDisease.setDiseaseCode("R06.02");
        secondaryDisease.setStatus("SECONDARY");
        secondaryDisease.setRemarks("Non-cardiac chest pain, likely musculoskeletal");
        secondaryDisease.setCreatedBy("DR001");
        secondaryDisease.setDateCreated(getCurrentDateForApi());
        secondaryDisease.setLastModified(getCurrentDateForApi());
        secondaryDisease.setLastModifiedBy("DR001");
        
        return Arrays.asList(primaryDisease, secondaryDisease);
    }
    
    /**
     * Creates sample folio items (services and supplies)
     */
    private static List<FolioItem> createSampleFolioItems() {
        // Consultation fee
        FolioItem consultation = new FolioItem();
        consultation.setItemCode("CONS001");
        consultation.setItemName("General Medical Consultation");
        consultation.setItemTypeID(1); // Registration/Consultation
        consultation.setUnitPrice(25000.0);
        consultation.setItemQuantity(1);
        consultation.setAmountClaimed(25000.0);
        consultation.setOtherDetails("Initial consultation with comprehensive examination");
        consultation.setApprovalRefNo("APPR001");
        consultation.setCreatedBy("DR001");
        consultation.setDateCreated(getCurrentDateForApi());
        consultation.setLastModifiedBy("DR001");
        consultation.setLastModified(getCurrentDateForApi());
        
        // ECG procedure
        FolioItem ecg = new FolioItem();
        ecg.setItemCode("ECG001");
        ecg.setItemName("Electrocardiogram (ECG)");
        ecg.setItemTypeID(5); // Diagnostic Examinations
        ecg.setUnitPrice(35000.0);
        ecg.setItemQuantity(1);
        ecg.setAmountClaimed(35000.0);
        ecg.setOtherDetails("12-lead ECG for cardiac assessment");
        ecg.setApprovalRefNo("APPR002");
        ecg.setCreatedBy("TECH001");
        ecg.setDateCreated(getCurrentDateForApi());
        ecg.setLastModifiedBy("TECH001");
        ecg.setLastModified(getCurrentDateForApi());
        
        // Blood pressure medication
        FolioItem medication = new FolioItem();
        medication.setItemCode("MED001");
        medication.setItemName("Amlodipine 5mg Tablets");
        medication.setItemTypeID(3); // Medicine and Consumables
        medication.setUnitPrice(500.0);
        medication.setItemQuantity(30); // 30 tablets
        medication.setAmountClaimed(15000.0);
        medication.setOtherDetails("Anti-hypertensive medication, 30-day supply");
        medication.setApprovalRefNo("APPR003");
        medication.setCreatedBy("PHARM001");
        medication.setDateCreated(getCurrentDateForApi());
        medication.setLastModifiedBy("PHARM001");
        medication.setLastModified(getCurrentDateForApi());
        
        // Laboratory tests
        FolioItem labTests = new FolioItem();
        labTests.setItemCode("LAB001");
        labTests.setItemName("Complete Blood Count + Lipid Profile");
        labTests.setItemTypeID(5); // Diagnostic Examinations
        labTests.setUnitPrice(50000.0);
        labTests.setItemQuantity(1);
        labTests.setAmountClaimed(50000.0);
        labTests.setOtherDetails("Comprehensive blood work for cardiovascular assessment");
        labTests.setApprovalRefNo("APPR004");
        labTests.setCreatedBy("LAB001");
        labTests.setDateCreated(getCurrentDateForApi());
        labTests.setLastModifiedBy("LAB001");
        labTests.setLastModified(getCurrentDateForApi());
        
        return Arrays.asList(consultation, ecg, medication, labTests);
    }
    
    /**
     * Creates sample signatures for the folio
     */
    private static List<FolioSignature> createSampleSignatures() {
        // Doctor's signature
        FolioSignature doctorSignature = new FolioSignature();
        doctorSignature.setSignatory("Dr. Jane Smith");
        doctorSignature.setSignatoryID("DR001");
        doctorSignature.setSignatureData("iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg=="); // Base64 signature
        doctorSignature.setDateCreated(getCurrentDateForApi());
        doctorSignature.setCreatedBy("DR001");
        doctorSignature.setLastModified(getCurrentDateForApi());
        doctorSignature.setLastModifiedBy("DR001");
        
        // Patient's signature
        FolioSignature patientSignature = new FolioSignature();
        patientSignature.setSignatory("John Doe");
        patientSignature.setSignatoryID("NHIF123456789");
        patientSignature.setSignatureData("iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg=="); // Base64 signature
        patientSignature.setDateCreated(getCurrentDateForApi());
        patientSignature.setCreatedBy("SYSTEM");
        patientSignature.setLastModified(getCurrentDateForApi());
        patientSignature.setLastModifiedBy("SYSTEM");
        
        return Arrays.asList(doctorSignature, patientSignature);
    }
    
    /**
     * Utility method to create an inpatient folio submission
     */
    public static FolioSubmissionRequest createInpatientFolioSubmission(String facilityCode) {
        FolioSubmissionRequest folio = createSampleFolioSubmission(facilityCode);
        
        // Modify for inpatient scenario
        folio.setPatientTypeCode("INPATIENT");
        folio.setDateAdmitted(formatDateForApi(LocalDateTime.now().minusDays(3)));
        folio.setDateDischarged(formatDateForApi(LocalDateTime.now().minusDays(1)));
        folio.setAmountClaimed(450000.0); // Higher amount for inpatient care
        
        // Add inpatient-specific items
        FolioItem bedFee = new FolioItem();
        bedFee.setItemCode("BED001");
        bedFee.setItemName("General Ward Bed - Per Day");
        bedFee.setItemTypeID(2); // Inpatient Charges
        bedFee.setUnitPrice(75000.0);
        bedFee.setItemQuantity(3); // 3 days
        bedFee.setAmountClaimed(225000.0);
        bedFee.setOtherDetails("General ward accommodation for 3 days");
        bedFee.setApprovalRefNo("APPR005");
        bedFee.setCreatedBy("ADMIN001");
        bedFee.setDateCreated(getCurrentDateForApi());
        bedFee.setLastModifiedBy("ADMIN001");
        bedFee.setLastModified(getCurrentDateForApi());
        
        // Add the bed fee to existing items
        folio.getFolioItems().add(bedFee);
        
        return folio;
    }
    
    /**
     * Utility method to create an emergency folio submission
     */
    public static FolioSubmissionRequest createEmergencyFolioSubmission(String facilityCode) {
        FolioSubmissionRequest folio = createSampleFolioSubmission(facilityCode);
        
        // Modify for emergency scenario
        folio.setPatientTypeCode("EMERGENCY");
        folio.setClinicalNotes("EMERGENCY: Patient presented with severe chest pain and shortness of breath. " +
                              "Immediate cardiac assessment performed. Stabilized and referred for cardiology consultation.");
        folio.setAmountClaimed(275000.0);
        
        // Add emergency-specific disease
        FolioDisease emergencyDisease = new FolioDisease();
        emergencyDisease.setDiseaseCode("R06.00");
        emergencyDisease.setStatus("PRIMARY");
        emergencyDisease.setRemarks("Acute respiratory distress - emergency presentation");
        emergencyDisease.setCreatedBy("ER001");
        emergencyDisease.setDateCreated(getCurrentDateForApi());
        emergencyDisease.setLastModified(getCurrentDateForApi());
        emergencyDisease.setLastModifiedBy("ER001");
        
        folio.getFolioDiseases().add(emergencyDisease);
        
        return folio;
    }
    
    /**
     * Utility method to validate folio submission before sending
     */
    public static boolean validateFolioSubmission(FolioSubmissionRequest folio) {
        if (folio == null) return false;
        
        // Required fields validation
        if (folio.getFacilityCode() == null || folio.getFacilityCode().trim().isEmpty()) {
            System.err.println("Validation error: Facility code is required");
            return false;
        }
        
        if (folio.getCardNo() == null || folio.getCardNo().trim().isEmpty()) {
            System.err.println("Validation error: Card number is required");
            return false;
        }
        
        if (folio.getFirstName() == null || folio.getFirstName().trim().isEmpty()) {
            System.err.println("Validation error: First name is required");
            return false;
        }
        
        if (folio.getLastName() == null || folio.getLastName().trim().isEmpty()) {
            System.err.println("Validation error: Last name is required");
            return false;
        }
        
        if (folio.getAuthorizationNo() == null || folio.getAuthorizationNo().trim().isEmpty()) {
            System.err.println("Validation error: Authorization number is required");
            return false;
        }
        
        if (folio.getFolioItems() == null || folio.getFolioItems().isEmpty()) {
            System.err.println("Validation error: At least one folio item is required");
            return false;
        }
        
        if (folio.getFolioDiseases() == null || folio.getFolioDiseases().isEmpty()) {
            System.err.println("Validation error: At least one diagnosis is required");
            return false;
        }
        
        // Amount validation
        if (folio.getAmountClaimed() == null || folio.getAmountClaimed() <= 0) {
            System.err.println("Validation error: Amount claimed must be greater than zero");
            return false;
        }
        
        // Calculate total from items
        double calculatedTotal = folio.getFolioItems().stream()
            .mapToDouble(item -> item.getAmountClaimed() != null ? item.getAmountClaimed() : 0.0)
            .sum();
        
        if (Math.abs(calculatedTotal - folio.getAmountClaimed()) > 0.01) {
            System.err.printf("Validation warning: Amount claimed (%.2f) doesn't match sum of items (%.2f)\n", 
                folio.getAmountClaimed(), calculatedTotal);
        }
        
        System.out.println("Folio validation passed");
        return true;
    }
}