package com.oau.nhif.example;

import com.oau.nhif.client.NhifApiClient;
import com.oau.nhif.client.NhifApiClientFactory;
import com.oau.nhif.client.model.FacilityPackageItem;
import com.oau.nhif.client.model.PackageItem;
import com.oau.nhif.client.model.ItemType;
import com.oau.nhif.exception.NhifApiException;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Example class demonstrating how to use the facility-specific price package API
 * This shows how to get package items that are available for a specific facility
 */
public class FacilityPackageExample {
    
    public static void main(String[] args) {
        // Initialize the client with your credentials
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
            
            System.out.println("=== NHIF Facility-Specific Package Example ===\n");
            
            // Get item types for reference
            System.out.println("1. Fetching item types for reference...");
            List<ItemType> itemTypes = client.getItemTypes().get();
            System.out.println("Total item types available: " + itemTypes.size());
            
            // Get all package items
            System.out.println("\n2. Fetching all package items...");
            List<PackageItem> allItems = client.getPackageItems().get();
            System.out.println("Total package items in system: " + allItems.size());
            
            // Get facility-specific package items
            System.out.println("\n3. Fetching facility-specific package items...");
            List<FacilityPackageItem> facilityItems = client.getPricePackageByFacility(clientId).get();
            System.out.printf("Package items available for facility %s: %d\n", clientId, facilityItems.size());
            
            if (facilityItems.isEmpty()) {
                System.out.println("No package items found for this facility.");
                return;
            }
            
            // Calculate coverage percentage
            double coveragePercentage = (facilityItems.size() * 100.0) / allItems.size();
            System.out.printf("Facility coverage: %.1f%% of all available items\n", coveragePercentage);
            
            // Analyze facility items by type
            System.out.println("\n4. Analyzing facility items by type...");
            itemTypes.forEach(itemType -> {
                long facilityCount = facilityItems.stream()
                    .filter(item -> item.getItemTypeID() != null && 
                                  item.getItemTypeID().equals(itemType.getItemTypeID()))
                    .count();
                long totalCount = allItems.stream()
                    .filter(item -> item.getItemTypeID() != null && 
                                  item.getItemTypeID().equals(itemType.getItemTypeID()))
                    .count();
                
                if (facilityCount > 0 || totalCount > 0) {
                    double typePercentage = totalCount > 0 ? (facilityCount * 100.0 / totalCount) : 0;
                    System.out.printf("  %s: %d/%d items (%.1f%%)\n", 
                        itemType.getTypeName(), facilityCount, totalCount, typePercentage);
                }
            });
            
            // Show high-value items available at facility
            System.out.println("\n5. High-value items (>500,000 TSh) available at facility...");
            List<FacilityPackageItem> highValueItems = facilityItems.stream()
                .filter(item -> item.getUnitPrice() != null && item.getUnitPrice() > 500000)
                .sorted((a, b) -> Double.compare(b.getUnitPrice(), a.getUnitPrice()))
                .limit(5)
                .collect(Collectors.toList());
            
            if (highValueItems.isEmpty()) {
                System.out.println("No high-value items found for this facility.");
            } else {
                highValueItems.forEach(item -> {
                    System.out.printf("  %s - %s\n", item.getItemCode(), item.getItemName());
                    System.out.printf("    Price: TSh %,.2f, Coverage: %d%%, Type: %d\n", 
                        item.getUnitPrice(), item.getUnitPrice(), item.getItemTypeID());
                });
            }
            

            // Show medication items at facility
            System.out.println("7. Medication items with dosage info at facility...");
            List<FacilityPackageItem> medications = facilityItems.stream()
                .filter(item -> item.getDosage() != null && !item.getDosage().trim().isEmpty())
                .limit(3)
                .collect(Collectors.toList());
            
            if (medications.isEmpty()) {
                System.out.println("No medication items with dosage info found for this facility.");
            } else {
                medications.forEach(item -> {
                    System.out.printf("  %s - %s\n", item.getItemCode(), item.getItemName());
                    System.out.printf("    Strength: %s, Dosage: %s\n", 
                        item.getStrength(), item.getDosage());
                    System.out.printf("    Calculated Per Day: %s, Coverage: %d%%\n", 
                        item.getPriceCode(), item.getMaximumQuantity());
                    System.out.println();
                });
            }
            

            System.out.println("\n=== Facility Package Example Complete ===");
            
        } catch (NhifApiException | InterruptedException | ExecutionException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}