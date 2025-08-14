package com.oau.nhif.example;

import com.oau.nhif.client.NhifApiClient;
import com.oau.nhif.client.NhifApiClientFactory;
import com.oau.nhif.client.NhifApiConfig;
import com.oau.nhif.client.DefaultNhifApiClient;
import com.oau.nhif.client.model.PractitionerAttendanceRequest;
import com.oau.nhif.client.model.GenericResponse;
import com.oau.nhif.exception.NhifApiException;

import java.util.concurrent.ExecutionException;

/**
 * Example class demonstrating how to use the NHIF Attendance API
 * This covers practitioner attendance operations including:
 * - Practitioner login with biometric data
 * - Practitioner logout
 */
public class AttendanceApiExample {
    
    public static void main(String[] args) {
        // Initialize the client with your credentials
        String authBaseUrl = "https://verification.nhif.or.tz";
        String serviceBaseUrl = "https://test.nhif.or.tz/servicehub";  // Use HTTPS
        String clientId = "11014";
        String clientSecret = "ntbzRGbrwwHj8Jwd7bbPsg==";
        String username = "Mtundi";
        
        // Option 1: Use simple factory method (SSL verification disabled by default)
        try (NhifApiClient client = NhifApiClientFactory.createClient(
                authBaseUrl, 
                serviceBaseUrl,
                clientId, 
                clientSecret, 
                username)) {
            
            System.out.println("=== NHIF Attendance API Example ===\n");
            
            // Test practitioner login
            testPractitionerLogin(client);
            
            // Test practitioner logout
            testPractitionerLogout(client);
            
            System.out.println("\n=== Attendance API Example Complete ===");
            
        } catch (NhifApiException  e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Example method demonstrating practitioner attendance login
     */
    public static void testPractitionerLogin(NhifApiClient client) {
        try {
            System.out.println("1. Testing Practitioner Login...");
            
            // Create sample attendance request
            PractitionerAttendanceRequest request = new PractitionerAttendanceRequest();
            request.setNationalID("12312321");
            request.setPractitionerNo("1321321");
            request.setBiometricMethod("false");
            request.setFpCode("string");
            request.setImageData("string");
            
            System.out.println("Practitioner Login Request:");
            System.out.printf("  National ID: %s\n", request.getNationalID());
            System.out.printf("  Practitioner No: %s\n", request.getPractitionerNo());
            System.out.printf("  Biometric Method: %s\n", request.getBiometricMethod());
            System.out.printf("  FP Code: %s\n", request.getFpCode());
            System.out.printf("  Image Data: %s\n", request.getImageData());
            
            // Submit the attendance request
            GenericResponse response = client.loginPractitioner(request).get();
            
            System.out.println("\nPractitioner Login Response:");
            if (response != null) {
                System.out.printf("  Message: %s\n", response.getMessage());
                System.out.printf("  Success: %s\n", response.getSuccess());
                System.out.printf("  Data: %s\n", response.getData());
            } else {
                System.out.println("  No response received");
            }
            
        } catch (Exception e) {
            System.err.println("Error testing practitioner login: " + e.getMessage());
            System.out.println("This is expected if the endpoint requires valid credentials or is not available in test environment");
        }
    }
    
    /**
     * Example method demonstrating practitioner logout
     */
    public static void testPractitionerLogout(NhifApiClient client) {
        try {
            System.out.println("\n2. Testing Practitioner Logout...");
            
            // Sample logout parameters
            String practitionerNo = "12423432";
            String facilityCode = "1001010";
            
            System.out.println("Practitioner Logout Request:");
            System.out.printf("  Practitioner No: %s\n", practitionerNo);
            System.out.printf("  Facility Code: %s\n", facilityCode);
            
            // Submit the logout request
            GenericResponse response = client.logoutPractitioner(practitionerNo, facilityCode).get();
            
            System.out.println("\nPractitioner Logout Response:");
            if (response != null) {
                System.out.printf("  Message: %s\n", response.getMessage());
                System.out.printf("  Success: %s\n", response.getSuccess());
                System.out.printf("  Data: %s\n", response.getData());
            } else {
                System.out.println("  No response received");
            }
            
        } catch (Exception e) {
            System.err.println("Error testing practitioner logout: " + e.getMessage());
            System.out.println("This is expected if the endpoint requires valid credentials or is not available in test environment");
        }
    }
    
    /**
     * Utility method to demonstrate complete attendance workflow
     */
    public static void demonstrateAttendanceWorkflow(NhifApiClient client) {
        System.out.println("=== Complete Attendance Workflow ===");
        
        try {
            // Step 1: Login practitioner
            System.out.println("Step 1: Logging in practitioner...");
            PractitionerAttendanceRequest loginRequest = new PractitionerAttendanceRequest(
                "12312321", "1321321", "false", "string", "string"
            );
            
            GenericResponse loginResponse = client.loginPractitioner(loginRequest).get();
            boolean loginSuccessful = loginResponse != null && 
                                    loginResponse.getSuccess() != null && 
                                    loginResponse.getSuccess();
            
            System.out.printf("Login result: %s\n", loginSuccessful ? "SUCCESS" : "FAILED");
            
            if (loginSuccessful) {
                System.out.println("Practitioner is now logged in and can perform duties...");
                
                // Step 2: Logout practitioner
                System.out.println("\nStep 2: Logging out practitioner...");
                GenericResponse logoutResponse = client.logoutPractitioner("1321321", "11014").get();
                boolean logoutSuccessful = logoutResponse != null && 
                                         logoutResponse.getSuccess() != null && 
                                         logoutResponse.getSuccess();
                
                System.out.printf("Logout result: %s\n", logoutSuccessful ? "SUCCESS" : "FAILED");
            }
            
        } catch (Exception e) {
            System.err.println("Error in attendance workflow: " + e.getMessage());
        }
        
        System.out.println("=== Attendance Workflow Complete ===");
    }
    
    /**
     * Alternative main method showing explicit SSL configuration
     * (SSL verification is now disabled by default, but this shows how to configure it explicitly)
     */
    public static void mainWithExplicitSslConfig(String[] args) {
        // Initialize the client with SSL verification disabled
        String authBaseUrl = "https://verification.nhif.or.tz";
        String serviceBaseUrl = "https://test.nhif.or.tz/servicehub";  // Use HTTPS
        String clientId = "11014";
        String clientSecret = "ntbzRGbrwwHj8Jwd7bbPsg==";
        String username = "Mtundi";
        
        try {
            // Option 2: Use configuration builder with explicit SSL configuration
            NhifApiConfig config = NhifApiConfig.builder()
                    .authBaseUrl(authBaseUrl)
                    .serviceBaseUrl(serviceBaseUrl)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .username(username)
                    .skipSslVerification(true)  // Explicit SSL skip (now default)
                    .enableLogging(true)        // Enable logging for debugging
                    .build();
                    
            try (NhifApiClient client = new DefaultNhifApiClient(config)) {
                System.out.println("=== NHIF Attendance API Example (SSL Skip) ===\n");
                
                // Test practitioner login
                testPractitionerLogin(client);
                
                // Test practitioner logout
                testPractitionerLogout(client);
                
                System.out.println("\n=== Attendance API Example Complete ===");
            }
            
        } catch (NhifApiException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}