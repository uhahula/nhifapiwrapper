package com.oau.nhif.example;

import com.oau.nhif.client.NhifApiClient;
import com.oau.nhif.client.NhifApiClientFactory;
import com.oau.nhif.client.model.*;
import com.oau.nhif.exception.NhifApiException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Example class demonstrating how to use the NHIF Admissions API
 * This covers the complete patient admission workflow including:
 * - Getting reference data (admission types, ward types, etc.)
 * - Admitting a patient
 * - Transferring a patient between wards
 * - Discharging a patient
 * - Querying admitted patients
 */
public class AdmissionsApiExample {
    
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
            
            System.out.println("=== NHIF Admissions API Example ===\n");
            
            // Step 1: Get reference data needed for admission
            System.out.println("1. Fetching reference data...");
            
            // Get admission types
            List<AdmissionType> admissionTypes = client.getAdmissionTypes().get();
            System.out.println("Available admission types:");
            admissionTypes.forEach(type -> 
                System.out.printf("  - ID: %d, Name: %s\n", 
                    type.getAdmissionTypeID(), type.getAdmissionTypeName()));
            
            // Get ward types
            List<WardType> wardTypes = client.getWardTypes().get();
            System.out.println("Available ward types:");
            wardTypes.forEach(type -> 
                System.out.printf("  - ID: %d, Name: %s\n", 
                    type.getWardTypeID(), type.getWardTypeName()));
            
            // Get room types
            List<RoomType> roomTypes = client.getRoomTypes().get();
            System.out.println("Available room types:");
            roomTypes.forEach(type -> 
                System.out.printf("  - ID: %d, Name: %s\n", 
                    type.getRoomTypeID(), type.getRoomTypeName()));
            
            // Get discharge types
            List<DischargeType> dischargeTypes = client.getDischargeTypes().get();
            System.out.println("Available discharge types:");
            dischargeTypes.forEach(type -> 
                System.out.printf("  - ID: %d, Name: %s\n", 
                    type.getDischargeTypeID(), type.getDischargeTypeName()));
            
            //System.out.println("\n" + "=".repeat(50) + "\n");
            // Step 2: Admit a patient
            System.out.println("2. Admitting a patient...");
            
            PatientAdmissionModel admission = new PatientAdmissionModel();
            admission.setAuthorizationNo("AUTH123456789");
            admission.setFullName("John Doe");
            admission.setGender("M");
            admission.setDateOfBirth(formatDateForApi(LocalDateTime.of(1985, 3, 15, 0, 0)));
            admission.setAdmissionTypeID(admissionTypes.isEmpty() ? 1 : admissionTypes.get(0).getAdmissionTypeID());
            admission.setWardTypeID(wardTypes.isEmpty() ? 1 : wardTypes.get(0).getWardTypeID());
            admission.setRoomTypeID(roomTypes.isEmpty() ? 1 : roomTypes.get(0).getRoomTypeID());
            admission.setChargesPerDay(50000.0); // TSh 50,000 per day
            admission.setPractitionerNo("DR001");
            admission.setDiagnosisAtAdmission("Pneumonia");
            admission.setPractitionersRemarks("Patient admitted for pneumonia treatment");
            admission.setDateAdmitted(getCurrentDateForApi());
            admission.setCreatedBy("SYSTEM");
            
            GenericResponse admissionResponse = client.admitPatient(admission).get();
            System.out.println("Admission response: " + admissionResponse.getMessage());
            
            // Assume we got an admission number back
            String admissionNo = "ADM" + System.currentTimeMillis();
            System.out.println("Patient admitted with admission number: " + admissionNo);
            
           // System.out.println("\n" + "=".repeat(50) + "\n");
            
            // Step 3: Query admitted patients
            System.out.println("3. Querying admitted patients...");
            
            List<AdmittedPatient> allAdmittedPatients = client.getAdmittedPatients().get();
            System.out.println("Total admitted patients: " + allAdmittedPatients.size());
            
            // Query by facility
            List<AdmittedPatient> facilityPatients = client.getAdmittedPatientsByFacility(clientId).get();
            System.out.println("Admitted patients in our facility: " + facilityPatients.size());
            
            if (!facilityPatients.isEmpty()) {
                AdmittedPatient patient = facilityPatients.get(0);
                System.out.printf("Example patient: %s (Admission: %s, Ward: %d)\n",
                    patient.getFullName(), patient.getAdmissionNo(), patient.getWardTypeID());
            }

            //System.out.println("\n" + "=".repeat(50) + "\n");
            // Step 4: Transfer patient to different ward
            System.out.println("4. Transferring patient...");
            
            PatientTransferModel transfer = new PatientTransferModel();
            transfer.setAdmissionNo(admissionNo);
            transfer.setPractitionerNo("DR002");
            transfer.setPractitionersRemarks("Transfer to ICU for intensive monitoring");

            // Transfer to different ward/room (assume ICU is ward type 2)
            transfer.setWardTypeID(wardTypes.size() > 1 ? wardTypes.get(1).getWardTypeID() : 2);
            transfer.setRoomTypeID(roomTypes.size() > 1 ? roomTypes.get(1).getRoomTypeID() : 2);
            transfer.setChargesPerDay(100000.0); // TSh 100,000 per day for ICU
            transfer.setDateTransferred(getCurrentDateForApi());
            transfer.setCreatedBy("SYSTEM");
            
            GenericResponse transferResponse = client.transferPatient(transfer).get();
            System.out.println("Transfer response: " + transferResponse.getMessage());
            // System.out.println("\n" + "=".repeat(50) + "\n");
            // Step 5: Discharge patient
            System.out.println("5. Discharging patient...");
            
            PatientDischargeModel discharge = new PatientDischargeModel();
            discharge.setAdmissionNo(admissionNo);
            discharge.setPractitionerNo("DR001");
            discharge.setPractitionersRemarks("Patient recovered well, discharge home");
            discharge.setDischargeTypeID(dischargeTypes.isEmpty() ? 1 : dischargeTypes.get(0).getDischargeTypeID());
            discharge.setDateDischarged(getCurrentDateForApi());
            discharge.setDiagnosisAtDischarge("Pneumonia - Resolved");
            discharge.setReferredToFacilityCode(null); // Not referred, going home
            discharge.setCreatedBy("SYSTEM");
            
            GenericResponse dischargeResponse = client.dischargePatient(discharge).get();
            System.out.println("Discharge response: " + dischargeResponse.getMessage());
            
            //System.out.println("\n" + "=".repeat(50) + "\n");
            // Step 6: Query admission details by authorization number
            System.out.println("6. Querying admission details...");
            
            try {
                GenericResponse admissionDetails = client.getAdmissionDetailsByAuthorizationNo("480527859458").get();
                System.out.println("Admission details: " + admissionDetails.getMessage());
            } catch (Exception e) {
                System.out.println("Could not retrieve admission details: " + e.getMessage());
            }
            
            // Step 7: Get admission types by product code
            System.out.println("\n7. Getting admission types by product code...");
            
            try {
                List<AdmissionType> productAdmissionTypes = client.getAdmissionTypesByProductCode("PROD001").get();
                System.out.println("Admission types for product PROD001: " + productAdmissionTypes.size());
            } catch (Exception e) {
                System.out.println("Could not retrieve admission types by product: " + e.getMessage());
            }
            
            System.out.println("\n=== Admissions API Example Complete ===");
            
        } catch (NhifApiException | InterruptedException | ExecutionException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Utility method to demonstrate how to handle admission workflow step by step
     */
    public static void demonstrateAdmissionWorkflow(NhifApiClient client, String authorizationNo) 
            throws InterruptedException, ExecutionException, NhifApiException {
        
        System.out.println("=== Complete Admission Workflow Example ===");
        
        // 1. Verify patient authorization before admission
        System.out.println("Step 1: Verifying authorization...");
        try {
            GenericResponse authDetails = client.getDetailsByAuthorizationNo(authorizationNo).get();
            System.out.println("Authorization verified: " + authDetails.getMessage());
        } catch (Exception e) {
            System.out.println("Authorization verification failed: " + e.getMessage());
            return;
        }
        
        // 2. Get available resources
        System.out.println("Step 2: Checking available resources...");

        // Get available wards and rooms
        List<WardType> availableWards;
        List<RoomType> availableRooms;
        
        try {
            availableWards = client.getWardTypes().get();
            availableRooms = client.getRoomTypes().get();
        } catch (Exception e) {
            System.out.println("Failed to get ward/room types: " + e.getMessage());
            return;
        }
        
        if (availableWards.isEmpty() || availableRooms.isEmpty()) {
            System.out.println("No available wards or rooms for admission");
            return;
        }

        // 3. Admit patient
        System.out.println("Step 3: Admitting patient...");
        PatientAdmissionModel admission = createSampleAdmission(authorizationNo, availableWards, availableRooms);

        // Admit patient
        try {
            GenericResponse admissionResult = client.admitPatient(admission).get();
            System.out.println("Admission result: " + admissionResult.getMessage());
        } catch (Exception e) {
            System.out.println("Admission failed: " + e.getMessage());
            return;
        }

        // 4. Monitor admission status
        System.out.println("Step 4: Monitoring admission...");

        try {
            // Monitor admission status
            List<AdmittedPatient> currentPatients = client.getAdmittedPatients().get();
            System.out.println("Current admitted patients: " + currentPatients.size());
        } catch (Exception e) {
            System.out.println("Failed to get admitted patients: " + e.getMessage());
        }

        // 5. Discharge patient
        System.out.println("Step 5: Discharging patient...");
        // Discharge patient
        System.out.println("Workflow completed successfully!");

    }
    
    /**
     * Helper method to create a sample admission request
     */
    private static PatientAdmissionModel createSampleAdmission(String authorizationNo, 
            List<WardType> wards, List<RoomType> rooms) {
        
        PatientAdmissionModel admission = new PatientAdmissionModel();
        admission.setAuthorizationNo(authorizationNo);
        admission.setFullName("Jane Smith");
        admission.setGender("F");
        admission.setDateOfBirth(formatDateForApi(LocalDateTime.of(1990, 6, 20, 0, 0)));
        admission.setAdmissionTypeID(1); // Emergency admission
        admission.setWardTypeID(wards.get(0).getWardTypeID());
        admission.setRoomTypeID(rooms.get(0).getRoomTypeID());
        admission.setChargesPerDay(75000.0);
        admission.setPractitionerNo("DR003");
        admission.setDiagnosisAtAdmission("Acute appendicitis");
        admission.setPractitionersRemarks("Emergency admission for appendectomy");
        admission.setDateAdmitted(getCurrentDateForApi());
        admission.setCreatedBy("ADMISSION_SYSTEM");
        
        return admission;
    }
}