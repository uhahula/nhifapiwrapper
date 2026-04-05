package com.oau.nhif.example;

import com.oau.nhif.client.NhifApiClient;
import com.oau.nhif.client.NhifApiClientFactory;
import com.oau.nhif.client.model.Disease;
import com.oau.nhif.exception.NhifApiException;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Example class demonstrating how to use the NHIF Diseases API
 * This covers retrieving disease reference data including:
 * - Disease codes and names
 * - ICD version information
 * - Non-specific disease indicators
 * - Disease creation and modification history
 */
public class DiseasesApiExample {
    
    public static void main(String[] args) {
        // Initialize the client with your credentials
        String authBaseUrl = "https://test.verification.nhif.or.tz";
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
            
            System.out.println("=== NHIF Diseases API Example ===\n");
            
            // Get all diseases
            System.out.println("1. Fetching all diseases...");
            
            List<Disease> diseases = client.getDiseases().get();
            System.out.println("Total diseases found: " + diseases.size());
            
            if (diseases.isEmpty()) {
                System.out.println("No diseases found.");
                return;
            }
            
            // Display first few diseases as examples
            System.out.println("\nFirst 10 diseases:");
            diseases.stream()
                .limit(10)
                .forEach(disease -> {
                    System.out.printf("  Code: %s - %s\n", 
                        disease.getDiseaseCode(), disease.getDiseaseName());
                    System.out.printf("    ICD Version: %s, Non-Specific: %s\n", 
                        disease.getIcdVersionCode(), disease.getIsNonSpecific());
                });
            
            // Analyze by ICD version
            System.out.println("\n2. Analyzing diseases by ICD version...");
            diseases.stream()
                .filter(disease -> disease.getIcdVersionCode() != null)
                .collect(java.util.stream.Collectors.groupingBy(
                    Disease::getIcdVersionCode,
                    java.util.stream.Collectors.counting()))
                .entrySet().stream()
                .forEach(entry -> {
                    System.out.printf("  %s: %d diseases\n", 
                        entry.getKey(), entry.getValue());
                });
            
            // Find non-specific diseases
            System.out.println("\n3. Non-specific diseases...");
            List<Disease> nonSpecificDiseases = diseases.stream()
                .filter(disease -> Boolean.TRUE.equals(disease.getIsNonSpecific()))
                .limit(5)
                .collect(Collectors.toList());
            
            if (nonSpecificDiseases.isEmpty()) {
                System.out.println("No non-specific diseases found.");
            } else {
                nonSpecificDiseases.forEach(disease -> {
                    System.out.printf("  %s - %s\n", 
                        disease.getDiseaseCode(), disease.getDiseaseName());
                });
            }
            
            // Search for specific diseases (common conditions)
            System.out.println("\n4. Common diseases search...");
            String[] searchTerms = {"Malaria", "Diabetes", "Hypertension", "Pneumonia", "Tuberculosis"};
            
            for (String searchTerm : searchTerms) {
                List<Disease> matchingDiseases = diseases.stream()
                    .filter(disease -> disease.getDiseaseName() != null && 
                                     disease.getDiseaseName().toLowerCase().contains(searchTerm.toLowerCase()))
                    .limit(3)
                    .collect(Collectors.toList());
                
                if (!matchingDiseases.isEmpty()) {
                    System.out.printf("  %s-related diseases:\n", searchTerm);
                    matchingDiseases.forEach(disease -> {
                        System.out.printf("    %s - %s\n", 
                            disease.getDiseaseCode(), disease.getDiseaseName());
                    });
                }
            }
            
            // Find diseases by code patterns
            System.out.println("\n5. Diseases by code patterns...");
            
            // Find diseases with specific code patterns
            System.out.println("Diseases with 3-digit codes (000-099):");
            diseases.stream()
                .filter(disease -> disease.getDiseaseCode() != null && 
                                 disease.getDiseaseCode().matches("^0\\d{2}$"))
                .limit(5)
                .forEach(disease -> {
                    System.out.printf("  %s - %s\n", 
                        disease.getDiseaseCode(), disease.getDiseaseName());
                });
            
            System.out.println("\nDiseases with codes starting with '1':");
            diseases.stream()
                .filter(disease -> disease.getDiseaseCode() != null && 
                                 disease.getDiseaseCode().startsWith("1"))
                .limit(5)
                .forEach(disease -> {
                    System.out.printf("  %s - %s\n", 
                        disease.getDiseaseCode(), disease.getDiseaseName());
                });
            
            // Analyze creation and modification patterns
            System.out.println("\n6. Disease creation and modification analysis...");
            
            // Group by creator
            diseases.stream()
                .filter(disease -> disease.getCreatedBy() != null)
                .collect(java.util.stream.Collectors.groupingBy(
                    Disease::getCreatedBy,
                    java.util.stream.Collectors.counting()))
                .entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(5)
                .forEach(entry -> {
                    System.out.printf("  Created by %s: %d diseases\n", 
                        entry.getKey(), entry.getValue());
                });
            
            // Find recently modified diseases
            System.out.println("\n7. Recently modified diseases...");
            List<Disease> recentlyModified = diseases.stream()
                .filter(disease -> disease.getLastModified() != null)
                .sorted((a, b) -> b.getLastModified().compareTo(a.getLastModified()))
                .limit(5)
                .collect(Collectors.toList());
            
            recentlyModified.forEach(disease -> {
                System.out.printf("  %s - %s\n", 
                    disease.getDiseaseCode(), disease.getDiseaseName());
                System.out.printf("    Last Modified: %s by %s\n", 
                    disease.getLastModified(), disease.getLastModifiedBy());
            });
            
            // Disease code validation patterns
            System.out.println("\n8. Disease code patterns analysis...");
            
            long numericCodes = diseases.stream()
                .filter(disease -> disease.getDiseaseCode() != null && 
                                 disease.getDiseaseCode().matches("^\\d+$"))
                .count();
            
            long alphanumericCodes = diseases.stream()
                .filter(disease -> disease.getDiseaseCode() != null && 
                                 disease.getDiseaseCode().matches("^[A-Z]\\d+"))
                .count();
            
            System.out.printf("Numeric codes: %d\n", numericCodes);
            System.out.printf("Alphanumeric codes (letter + numbers): %d\n", alphanumericCodes);
            System.out.printf("Other code patterns: %d\n", 
                diseases.size() - numericCodes - alphanumericCodes);
            
            System.out.println("\n=== Diseases API Example Complete ===");
            
        } catch (NhifApiException | InterruptedException | ExecutionException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Utility method to search for diseases by name
     */
    public static List<Disease> searchDiseasesByName(List<Disease> diseases, String searchTerm) {
        return diseases.stream()
            .filter(disease -> disease.getDiseaseName() != null && 
                              disease.getDiseaseName().toLowerCase().contains(searchTerm.toLowerCase()))
            .collect(Collectors.toList());
    }
    
    /**
     * Utility method to find diseases by ICD version
     */
    public static List<Disease> getDiseasesByICDVersion(List<Disease> diseases, String icdVersion) {
        return diseases.stream()
            .filter(disease -> icdVersion.equals(disease.getIcdVersionCode()))
            .collect(Collectors.toList());
    }
    
    /**
     * Utility method to validate disease code format
     */
    public static boolean isValidDiseaseCode(String diseaseCode) {
        if (diseaseCode == null || diseaseCode.trim().isEmpty()) {
            return false;
        }
        
        // Common patterns for disease codes
        return diseaseCode.matches("^\\d{3}$") ||           // 3-digit numeric (ICD-9)
               diseaseCode.matches("^[A-Z]\\d{2}$") ||      // Letter + 2 digits (ICD-10)
               diseaseCode.matches("^[A-Z]\\d{2}\\.\\d$");  // Letter + 2 digits + decimal (ICD-10)
    }
    
    /**
     * Utility method to analyze disease statistics
     */
    public static void analyzeDiseaseStatistics(List<Disease> diseases) {
        System.out.println("=== Disease Statistics ===");
        
        long totalDiseases = diseases.size();
        long nonSpecificDiseases = diseases.stream()
            .filter(disease -> Boolean.TRUE.equals(disease.getIsNonSpecific()))
            .count();
        
        System.out.printf("Total Diseases: %d\n", totalDiseases);
        System.out.printf("Non-Specific Diseases: %d (%.1f%%)\n", 
            nonSpecificDiseases, (nonSpecificDiseases * 100.0 / totalDiseases));
        
        // ICD version distribution
        System.out.println("\nICD Version Distribution:");
        diseases.stream()
            .filter(disease -> disease.getIcdVersionCode() != null)
            .collect(java.util.stream.Collectors.groupingBy(
                Disease::getIcdVersionCode,
                java.util.stream.Collectors.counting()))
            .entrySet().stream()
            .forEach(entry -> {
                System.out.printf("  %s: %d diseases (%.1f%%)\n", 
                    entry.getKey(), entry.getValue(), 
                    (entry.getValue() * 100.0 / totalDiseases));
            });
        
        // Find most recent updates
        Disease mostRecentlyModified = diseases.stream()
            .filter(disease -> disease.getLastModified() != null)
            .max((a, b) -> a.getLastModified().compareTo(b.getLastModified()))
            .orElse(null);
        
        if (mostRecentlyModified != null) {
            System.out.printf("\nMost Recently Modified: %s - %s (Modified: %s)\n", 
                mostRecentlyModified.getDiseaseCode(), 
                mostRecentlyModified.getDiseaseName(),
                mostRecentlyModified.getLastModified());
        }
    }
}