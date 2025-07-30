package com.oau.nhif.example;

import com.oau.nhif.client.NhifApiClient;
import com.oau.nhif.client.NhifApiClientFactory;
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
            List<PackageItem> facilityItems = client.getPricePackageByFacility(clientId).get();
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
            List<PackageItem> highValueItems = facilityItems.stream()
                .filter(item -> item.getCommonPrice() != null && item.getCommonPrice() > 500000)
                .sorted((a, b) -> Double.compare(b.getCommonPrice(), a.getCommonPrice()))
                .limit(5)
                .collect(Collectors.toList());
            
            if (highValueItems.isEmpty()) {
                System.out.println("No high-value items found for this facility.");
            } else {
                highValueItems.forEach(item -> {
                    System.out.printf("  %s - %s\n", item.getItemCode(), item.getItemName());
                    System.out.printf("    Price: TSh %,.2f, Coverage: %d%%, Type: %d\n", 
                        item.getCommonPrice(), item.getPercentCovered(), item.getItemTypeID());
                });
            }
            
            // Show restricted items available at facility
            System.out.println("\n6. Restricted items available at facility...");
            List<PackageItem> restrictedItems = facilityItems.stream()
                .filter(item -> Boolean.TRUE.equals(item.getIsRestricted()))
                .limit(5)
                .collect(Collectors.toList());
            
            if (restrictedItems.isEmpty()) {
                System.out.println("No restricted items found for this facility.");
            } else {
                restrictedItems.forEach(item -> {
                    System.out.printf("  %s - %s\n", item.getItemCode(), item.getItemName());
                    System.out.printf("    Waiting Period: %d days, Coverage: %d%%\n", 
                        item.getWaitingPeriod(), item.getPercentCovered());
                    if (item.getEligibility() != null) {
                        System.out.printf("    Eligibility: %s\n", item.getEligibility());
                    }
                    System.out.println();
                });
            }
            
            // Show medication items at facility
            System.out.println("7. Medication items with dosage info at facility...");
            List<PackageItem> medications = facilityItems.stream()
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
                        item.getCalculatedPerDay(), item.getPercentCovered());
                    System.out.println();
                });
            }
            
            // Compare active vs inactive items
            System.out.println("8. Active vs Inactive items at facility...");
            long activeItems = facilityItems.stream()
                .filter(item -> Boolean.TRUE.equals(item.getIsActive()))
                .count();
            long inactiveItems = facilityItems.size() - activeItems;
            
            System.out.printf("Active items: %d (%.1f%%)\n", 
                activeItems, (activeItems * 100.0 / facilityItems.size()));
            System.out.printf("Inactive items: %d (%.1f%%)\n", 
                inactiveItems, (inactiveItems * 100.0 / facilityItems.size()));
            
            // Show items by service type
            System.out.println("\n9. Items by service type at facility...");
            facilityItems.stream()
                .filter(item -> item.getServiceTypeID() != null)
                .collect(java.util.stream.Collectors.groupingBy(
                    PackageItem::getServiceTypeID,
                    java.util.stream.Collectors.counting()))
                .entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(5)
                .forEach(entry -> {
                    System.out.printf("  Service Type %d: %d items\n", 
                        entry.getKey(), entry.getValue());
                });
            
            System.out.println("\n=== Facility Package Example Complete ===");
            
        } catch (NhifApiException | InterruptedException | ExecutionException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Utility method to compare two facilities' package coverage
     */
    public static void compareFacilities(NhifApiClient client, String facility1Code, String facility2Code) 
            throws InterruptedException, ExecutionException, NhifApiException {
        
        System.out.println("=== Comparing Facility Package Coverage ===");
        
        List<PackageItem> facility1Items = client.getPricePackageByFacility(facility1Code).get();
        List<PackageItem> facility2Items = client.getPricePackageByFacility(facility2Code).get();
        
        System.out.printf("Facility %s: %d items\n", facility1Code, facility1Items.size());
        System.out.printf("Facility %s: %d items\n", facility2Code, facility2Items.size());
        
        // Find common items
        List<String> facility1Codes = facility1Items.stream()
            .map(PackageItem::getItemCode)
            .collect(Collectors.toList());
        List<String> facility2Codes = facility2Items.stream()
            .map(PackageItem::getItemCode)
            .collect(Collectors.toList());
        
        long commonItems = facility1Codes.stream()
            .filter(facility2Codes::contains)
            .count();
        
        System.out.printf("Common items: %d\n", commonItems);
        System.out.printf("Facility %s unique items: %d\n", 
            facility1Code, facility1Items.size() - commonItems);
        System.out.printf("Facility %s unique items: %d\n", 
            facility2Code, facility2Items.size() - commonItems);
    }
}