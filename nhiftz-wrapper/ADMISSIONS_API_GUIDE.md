# NHIF Admissions API Guide

This guide covers the newly implemented NHIF Admissions API endpoints and how to use them effectively.

## Overview

The Admissions API provides comprehensive functionality for managing patient admissions, transfers, and discharges in healthcare facilities. It includes endpoints for:

- **Reference Data**: Getting admission types, ward types, room types, and discharge types
- **Patient Management**: Admitting, transferring, and discharging patients
- **Query Operations**: Retrieving admitted patients and admission details

## Quick Start

```java
// Initialize the NHIF API client
try (NhifApiClient client = NhifApiClientFactory.createClient(
        authBaseUrl, serviceBaseUrl, clientId, clientSecret, username)) {
    
    // Get admission types
    List<AdmissionType> admissionTypes = client.getAdmissionTypes().get();
    
    // Admit a patient
    PatientAdmissionModel admission = new PatientAdmissionModel();
    admission.setAuthorizationNo("AUTH123456");
    admission.setFullName("John Doe");
    // ... set other required fields
    
    GenericResponse response = client.admitPatient(admission).get();
}
```

## API Endpoints

### Reference Data Endpoints

#### 1. Get Admission Types
```java
CompletableFuture<List<AdmissionType>> getAdmissionTypes()
```
Retrieves all available admission types.

**Example:**
```java
client.getAdmissionTypes()
    .thenAccept(types -> {
        types.forEach(type -> 
            System.out.println("ID: " + type.getAdmissionTypeID() + 
                             ", Name: " + type.getAdmissionTypeName()));
    });
```

#### 2. Get Ward Types
```java
CompletableFuture<List<WardType>> getWardTypes()
```
Retrieves all available ward types.

#### 3. Get Room Types
```java
CompletableFuture<List<RoomType>> getRoomTypes()
```
Retrieves all available room types.

#### 4. Get Discharge Types
```java
CompletableFuture<List<DischargeType>> getDischargeTypes()
```
Retrieves all available discharge types.

#### 5. Get Admission Types by Product Code
```java
CompletableFuture<List<AdmissionType>> getAdmissionTypesByProductCode(String productCode)
```
Retrieves admission types specific to a product code.

### Patient Management Endpoints

#### 1. Admit Patient
```java
CompletableFuture<GenericResponse> admitPatient(PatientAdmissionModel request)
```

**PatientAdmissionModel Properties:**
- `authorizationNo`: Patient authorization number (required)
- `fullName`: Patient full name (required)
- `gender`: Patient gender (M/F)
- `dateOfBirth`: Patient date of birth
- `admissionTypeID`: Admission type ID
- `wardTypeID`: Ward type ID
- `roomTypeID`: Room type ID
- `chargesPerDay`: Daily charges
- `practitionerNo`: Practitioner number
- `diagnosisAtAdmission`: Diagnosis at admission
- `practitionersRemarks`: Practitioner remarks
- `dateAdmitted`: Admission date
- `createdBy`: User who created the record

**Example:**
```java
PatientAdmissionModel admission = new PatientAdmissionModel();
admission.setAuthorizationNo("AUTH123456");
admission.setFullName("Jane Smith");
admission.setGender("F");
admission.setDateOfBirth(LocalDateTime.of(1990, 5, 15, 0, 0));
admission.setAdmissionTypeID(1);
admission.setWardTypeID(1);
admission.setRoomTypeID(1);
admission.setChargesPerDay(50000.0);
admission.setPractitionerNo("DR001");
admission.setDiagnosisAtAdmission("Pneumonia");
admission.setDateAdmitted(LocalDateTime.now());
admission.setCreatedBy("SYSTEM");

client.admitPatient(admission)
    .thenAccept(response -> 
        System.out.println("Admission result: " + response.getMessage()));
```

#### 2. Transfer Patient
```java
CompletableFuture<GenericResponse> transferPatient(PatientTransferModel request)
```

**PatientTransferModel Properties:**
- `admissionNo`: Admission number (required)
- `practitionerNo`: Practitioner number
- `practitionersRemarks`: Transfer remarks
- `wardTypeID`: New ward type ID
- `roomTypeID`: New room type ID
- `chargesPerDay`: New daily charges
- `dateTransferred`: Transfer date
- `createdBy`: User who created the record

**Example:**
```java
PatientTransferModel transfer = new PatientTransferModel();
transfer.setAdmissionNo("ADM123456");
transfer.setPractitionerNo("DR002");
transfer.setPractitionersRemarks("Transfer to ICU");
transfer.setWardTypeID(2); // ICU ward
transfer.setRoomTypeID(2); // ICU room
transfer.setChargesPerDay(100000.0);
transfer.setDateTransferred(LocalDateTime.now());
transfer.setCreatedBy("SYSTEM");

client.transferPatient(transfer)
    .thenAccept(response -> 
        System.out.println("Transfer result: " + response.getMessage()));
```

#### 3. Discharge Patient
```java
CompletableFuture<GenericResponse> dischargePatient(PatientDischargeModel request)
```

**PatientDischargeModel Properties:**
- `admissionNo`: Admission number (required)
- `practitionerNo`: Practitioner number
- `practitionersRemarks`: Discharge remarks
- `dischargeTypeID`: Discharge type ID
- `dateDischarged`: Discharge date
- `diagnosisAtDischarge`: Final diagnosis
- `referredToFacilityCode`: Facility code if referred
- `createdBy`: User who created the record

**Example:**
```java
PatientDischargeModel discharge = new PatientDischargeModel();
discharge.setAdmissionNo("ADM123456");
discharge.setPractitionerNo("DR001");
discharge.setPractitionersRemarks("Patient recovered, discharge home");
discharge.setDischargeTypeID(1);
discharge.setDateDischarged(LocalDateTime.now());
discharge.setDiagnosisAtDischarge("Pneumonia - Resolved");
discharge.setCreatedBy("SYSTEM");

client.dischargePatient(discharge)
    .thenAccept(response -> 
        System.out.println("Discharge result: " + response.getMessage()));
```

### Query Endpoints

#### 1. Get Admitted Patients
```java
CompletableFuture<List<AdmittedPatient>> getAdmittedPatients()
```
Retrieves all currently admitted patients.

#### 2. Get Admitted Patients by Facility
```java
CompletableFuture<List<AdmittedPatient>> getAdmittedPatientsByFacility(String facilityCode)
```
Retrieves admitted patients for a specific facility.

#### 3. Get Details by Authorization Number
```java
CompletableFuture<GenericResponse> getDetailsByAuthorizationNo(String authorizationNo)
```
Retrieves patient details using authorization number.

#### 4. Get Admission Details by Authorization Number
```java
CompletableFuture<GenericResponse> getAdmissionDetailsByAuthorizationNo(String authorizationNo)
```
Retrieves specific admission details using authorization number.

## Complete Workflow Example

Here's a complete example showing the typical admission workflow:

```java
public class AdmissionWorkflowExample {
    
    public static void performAdmissionWorkflow(NhifApiClient client) 
            throws InterruptedException, ExecutionException {
        
        // Step 1: Get reference data
        List<AdmissionType> admissionTypes = client.getAdmissionTypes().get();
        List<WardType> wardTypes = client.getWardTypes().get();
        List<RoomType> roomTypes = client.getRoomTypes().get();
        
        // Step 2: Verify patient authorization
        String authNo = "AUTH123456";
        GenericResponse authDetails = client.getDetailsByAuthorizationNo(authNo).get();
        
        // Step 3: Admit patient
        PatientAdmissionModel admission = new PatientAdmissionModel();
        admission.setAuthorizationNo(authNo);
        admission.setFullName("John Doe");
        admission.setGender("M");
        admission.setDateOfBirth(LocalDateTime.of(1985, 3, 15, 0, 0));
        admission.setAdmissionTypeID(admissionTypes.get(0).getAdmissionTypeID());
        admission.setWardTypeID(wardTypes.get(0).getWardTypeID());
        admission.setRoomTypeID(roomTypes.get(0).getRoomTypeID());
        admission.setChargesPerDay(50000.0);
        admission.setPractitionerNo("DR001");
        admission.setDiagnosisAtAdmission("Pneumonia");
        admission.setDateAdmitted(LocalDateTime.now());
        admission.setCreatedBy("SYSTEM");
        
        GenericResponse admissionResult = client.admitPatient(admission).get();
        String admissionNo = extractAdmissionNo(admissionResult);
        
        // Step 4: Monitor patient (optional transfers)
        // ... later if needed ...
        
        // Step 5: Discharge patient
        PatientDischargeModel discharge = new PatientDischargeModel();
        discharge.setAdmissionNo(admissionNo);
        discharge.setPractitionerNo("DR001");
        discharge.setPractitionersRemarks("Patient recovered");
        discharge.setDischargeTypeID(1);
        discharge.setDateDischarged(LocalDateTime.now());
        discharge.setDiagnosisAtDischarge("Pneumonia - Resolved");
        discharge.setCreatedBy("SYSTEM");
        
        GenericResponse dischargeResult = client.dischargePatient(discharge).get();
    }
    
    private static String extractAdmissionNo(GenericResponse response) {
        // Extract admission number from response
        // Implementation depends on actual response format
        return "ADM" + System.currentTimeMillis();
    }
}
```

## Error Handling

Always handle exceptions properly when using the API:

```java
try {
    List<AdmissionType> types = client.getAdmissionTypes().get();
    // Process types
} catch (ExecutionException e) {
    if (e.getCause() instanceof NhifApiException) {
        NhifApiException nhifError = (NhifApiException) e.getCause();
        System.err.println("NHIF API Error: " + nhifError.getMessage());
        System.err.println("Status Code: " + nhifError.getStatusCode());
    } else {
        System.err.println("Unexpected error: " + e.getMessage());
    }
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
    System.err.println("Operation interrupted");
}
```

## Asynchronous Operations

The API supports asynchronous operations. You can chain operations or run them concurrently:

```java
// Chain operations
client.getAdmissionTypes()
    .thenCompose(types -> {
        // Use types to determine admission type
        return client.getWardTypes();
    })
    .thenCompose(wards -> {
        // Use wards to create admission
        PatientAdmissionModel admission = createAdmission();
        return client.admitPatient(admission);
    })
    .thenAccept(result -> {
        System.out.println("Admission completed: " + result.getMessage());
    });

// Concurrent operations
CompletableFuture<List<AdmissionType>> admissionTypesFuture = client.getAdmissionTypes();
CompletableFuture<List<WardType>> wardTypesFuture = client.getWardTypes();
CompletableFuture<List<RoomType>> roomTypesFuture = client.getRoomTypes();

CompletableFuture.allOf(admissionTypesFuture, wardTypesFuture, roomTypesFuture)
    .thenRun(() -> {
        // All reference data loaded, proceed with admission
        try {
            List<AdmissionType> admissionTypes = admissionTypesFuture.get();
            List<WardType> wardTypes = wardTypesFuture.get();
            List<RoomType> roomTypes = roomTypesFuture.get();
            
            // Create and submit admission
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    });
```

## Best Practices

1. **Always fetch reference data first** before creating admissions to ensure valid IDs
2. **Use proper error handling** for all API calls
3. **Set appropriate timeouts** for your HTTP client
4. **Cache reference data** when possible to reduce API calls
5. **Use asynchronous operations** for better performance
6. **Validate input data** before sending to the API
7. **Log API interactions** for debugging and auditing

## Example Projects

See the following example files for complete implementations:

- `AdmissionsApiExample.java` - Complete workflow demonstration
- `AdmissionsApiTestExample.java` - Testing and error handling examples
- `TestExistingEndpoints.java` - API endpoint testing

## Support

For technical support or questions about the NHIF API, please contact the NHIF technical support team or refer to the official NHIF API documentation.