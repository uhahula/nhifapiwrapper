package com.oau.nhif.example;

import com.oau.nhif.client.NhifApiClient;
import com.oau.nhif.client.NhifApiClientFactory;
import com.oau.nhif.client.model.*;
import com.oau.nhif.exception.NhifApiException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Test example for NHIF Admissions API demonstrating various usage patterns
 * and error handling scenarios.
 */
public class AdmissionsApiTestExample {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Helper method to format date for .NET API compatibility
     * Format: "2025-07-07T00:41:22.783Z"
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
        // Test configuration
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
            
            System.out.println("=== NHIF Admissions API Test Examples ===\n");
            
            // Test 1: Basic API connectivity and reference data
            testReferenceDataApis(client);
            
            // Test 2: Patient admission flow
            testPatientAdmissionFlow(client);
            
            // Test 3: Error handling
            testErrorHandling(client);
            
            // Test 4: Asynchronous operations
            testAsynchronousOperations(client);
            
            System.out.println("\n=== All Tests Completed ===");
            
        } catch (Exception e) {
            System.err.println("Test failed with error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test reference data APIs
     */
    private static void testReferenceDataApis(NhifApiClient client) 
            throws InterruptedException, ExecutionException, NhifApiException {
        
        System.out.println("TEST 1: Reference Data APIs");
        System.out.println("----------------------------------------");
        
        // Test admission types
        List<AdmissionType> admissionTypes = client.getAdmissionTypes().get();
        System.out.printf("✓ Admission Types: %d items retrieved\n", admissionTypes.size());
        
        if (!admissionTypes.isEmpty()) {
            AdmissionType firstType = admissionTypes.get(0);
            System.out.printf("  Example: ID=%d, Name='%s'\n", 
                firstType.getAdmissionTypeID(), firstType.getAdmissionTypeName());
        }
        
        // Test ward types
        List<WardType> wardTypes = client.getWardTypes().get();
        System.out.printf("✓ Ward Types: %d items retrieved\n", wardTypes.size());
        
        // Test room types
        List<RoomType> roomTypes = client.getRoomTypes().get();
        System.out.printf("✓ Room Types: %d items retrieved\n", roomTypes.size());
        
        // Test discharge types
        List<DischargeType> dischargeTypes = client.getDischargeTypes().get();
        System.out.printf("✓ Discharge Types: %d items retrieved\n", dischargeTypes.size());
        
        System.out.println();
    }
    
    /**
     * Test complete patient admission flow
     */
    private static void testPatientAdmissionFlow(NhifApiClient client) 
            throws InterruptedException, ExecutionException, NhifApiException {
        
        System.out.println("TEST 2: Patient Admission Flow");
        System.out.println("----------------------------------------");
        
        String testAuthNo = "TEST_AUTH_" + System.currentTimeMillis();
        String testAdmissionNo = "ADM_" + System.currentTimeMillis();
        
        // Step 1: Create admission
        System.out.println("Step 1: Creating patient admission...");
        PatientAdmissionModel admission = createTestAdmission(testAuthNo);
        
        try {
            GenericResponse admissionResponse = client.admitPatient(admission).get();
            System.out.printf("✓ Admission created: %s\n", 
                admissionResponse.getMessage() != null ? admissionResponse.getMessage() : "Success");
        } catch (Exception e) {
            System.out.printf("⚠ Admission failed: %s\n", e.getMessage());
        }
        
        // Step 2: Query admitted patients
        System.out.println("Step 2: Querying admitted patients...");
        try {
            List<AdmittedPatient> patients = client.getAdmittedPatients().get();
            System.out.printf("✓ Found %d admitted patients\n", patients.size());
            
            // Try facility-specific query
            List<AdmittedPatient> facilityPatients = 
                client.getAdmittedPatientsByFacility("11014").get();
            System.out.printf("✓ Found %d patients in facility 11014\n", facilityPatients.size());
            
        } catch (Exception e) {
            System.out.printf("⚠ Query failed: %s\n", e.getMessage());
        }
        
        // Step 3: Transfer patient
        System.out.println("Step 3: Testing patient transfer...");
        PatientTransferModel transfer = createTestTransfer(testAdmissionNo);
        
        try {
            GenericResponse transferResponse = client.transferPatient(transfer).get();
            System.out.printf("✓ Transfer completed: %s\n", 
                transferResponse.getMessage() != null ? transferResponse.getMessage() : "Success");
        } catch (Exception e) {
            System.out.printf("⚠ Transfer failed: %s\n", e.getMessage());
        }
        
        // Step 4: Discharge patient
        System.out.println("Step 4: Testing patient discharge...");
        PatientDischargeModel discharge = createTestDischarge(testAdmissionNo);
        
        try {
            GenericResponse dischargeResponse = client.dischargePatient(discharge).get();
            System.out.printf("✓ Discharge completed: %s\n", 
                dischargeResponse.getMessage() != null ? dischargeResponse.getMessage() : "Success");
        } catch (Exception e) {
            System.out.printf("⚠ Discharge failed: %s\n", e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Test error handling scenarios
     */
    private static void testErrorHandling(NhifApiClient client) {
        System.out.println("TEST 3: Error Handling");
        System.out.println("----------------------------------------");
        
        // Test with invalid authorization number
        System.out.println("Testing invalid authorization number...");
        try {
            client.getAdmissionDetailsByAuthorizationNo("INVALID_AUTH_NO").get();
            System.out.println("⚠ Expected error but got success");
        } catch (Exception e) {
            System.out.printf("✓ Correctly handled invalid auth: %s\n", 
                e.getMessage().substring(0, Math.min(50, e.getMessage().length())));
        }
        
        // Test with invalid facility code
        System.out.println("Testing invalid facility code...");
        try {
            client.getAdmittedPatientsByFacility("INVALID_FACILITY").get();
            System.out.println("⚠ Expected error but got success");
        } catch (Exception e) {
            System.out.printf("✓ Correctly handled invalid facility: %s\n", 
                e.getMessage().substring(0, Math.min(50, e.getMessage().length())));
        }
        
        // Test with malformed admission data
        System.out.println("Testing malformed admission data...");
        try {
            PatientAdmissionModel malformedAdmission = new PatientAdmissionModel();
            // Missing required fields
            malformedAdmission.setFullName(null);
            malformedAdmission.setAuthorizationNo(null);
            
            client.admitPatient(malformedAdmission).get();
            System.out.println("⚠ Expected error but got success");
        } catch (Exception e) {
            System.out.printf("✓ Correctly handled malformed data: %s\n", 
                e.getMessage().substring(0, Math.min(50, e.getMessage().length())));
        }
        
        System.out.println();
    }
    
    /**
     * Test asynchronous operations and concurrency
     */
    private static void testAsynchronousOperations(NhifApiClient client) 
            throws InterruptedException, ExecutionException, NhifApiException {
        
        System.out.println("TEST 4: Asynchronous Operations");
        System.out.println("----------------------------------------");
        
        System.out.println("Testing concurrent API calls...");
        long startTime = System.currentTimeMillis();
        
        // Execute multiple API calls concurrently
        CompletableFuture<List<AdmissionType>> admissionTypesFuture;
        CompletableFuture<List<WardType>> wardTypesFuture;
        CompletableFuture<List<RoomType>> roomTypesFuture;
        CompletableFuture<List<DischargeType>> dischargeTypesFuture;
        
        try {
            admissionTypesFuture = client.getAdmissionTypes();
            wardTypesFuture = client.getWardTypes();
            roomTypesFuture = client.getRoomTypes();
            dischargeTypesFuture = client.getDischargeTypes();
        } catch (NhifApiException e) {
            System.out.printf("⚠ Failed to start concurrent operations: %s\n", e.getMessage());
            return;
        }
        
        // Wait for all to complete
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
            admissionTypesFuture, wardTypesFuture, roomTypesFuture, dischargeTypesFuture);
        
        allFutures.get();
        
        long duration = System.currentTimeMillis() - startTime;
        
        System.out.printf("✓ All concurrent calls completed in %d ms\n", duration);
        System.out.printf("  - Admission Types: %d\n", admissionTypesFuture.get().size());
        System.out.printf("  - Ward Types: %d\n", wardTypesFuture.get().size());
        System.out.printf("  - Room Types: %d\n", roomTypesFuture.get().size());
        System.out.printf("  - Discharge Types: %d\n", dischargeTypesFuture.get().size());
        
        // Test chained operations
        System.out.println("Testing chained operations...");
        
        try {
            List<AdmissionType> admissionTypes = client.getAdmissionTypes().get();
            System.out.println("✓ Got admission types, now getting ward types...");
            
            List<WardType> wardTypes = client.getWardTypes().get();
            System.out.println("✓ Got ward types, now getting room types...");
            
            List<RoomType> roomTypes = client.getRoomTypes().get();
            System.out.printf("✓ Chained operations completed. Final result: %d room types\n", 
                roomTypes.size());
        } catch (Exception e) {
            System.out.printf("⚠ Chained operations failed: %s\n", e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Helper method to create test admission
     */
    private static PatientAdmissionModel createTestAdmission(String authNo) {
        PatientAdmissionModel admission = new PatientAdmissionModel();
        admission.setAuthorizationNo(authNo);
        admission.setFullName("Test Patient " + System.currentTimeMillis());
        admission.setGender("M");
        admission.setDateOfBirth(formatDateForApi(LocalDateTime.of(1980, 1, 1, 0, 0)));
        admission.setAdmissionTypeID(1);
        admission.setWardTypeID(1);
        admission.setRoomTypeID(1);
        admission.setChargesPerDay(50000.0);
        admission.setPractitionerNo("TEST_DR");
        admission.setDiagnosisAtAdmission("Test diagnosis");
        admission.setPractitionersRemarks("Test admission");
        admission.setDateAdmitted(getCurrentDateForApi());
        admission.setCreatedBy("TEST_SYSTEM");
        return admission;
    }
    
    /**
     * Helper method to create test transfer
     */
    private static PatientTransferModel createTestTransfer(String admissionNo) {
        PatientTransferModel transfer = new PatientTransferModel();
        transfer.setAdmissionNo(admissionNo);
        transfer.setPractitionerNo("TEST_DR2");
        transfer.setPractitionersRemarks("Test transfer");
        transfer.setWardTypeID(2);
        transfer.setRoomTypeID(2);
        transfer.setChargesPerDay(75000.0);
        transfer.setDateTransferred(getCurrentDateForApi());
        transfer.setCreatedBy("TEST_SYSTEM");
        return transfer;
    }
    
    /**
     * Helper method to create test discharge
     */
    private static PatientDischargeModel createTestDischarge(String admissionNo) {
        PatientDischargeModel discharge = new PatientDischargeModel();
        discharge.setAdmissionNo(admissionNo);
        discharge.setPractitionerNo("TEST_DR");
        discharge.setPractitionersRemarks("Test discharge");
        discharge.setDischargeTypeID(1);
        discharge.setDateDischarged(getCurrentDateForApi());
        discharge.setDiagnosisAtDischarge("Test diagnosis - resolved");
        discharge.setCreatedBy("TEST_SYSTEM");
        return discharge;
    }
}