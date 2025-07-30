package com.oau.nhif.example;

import com.oau.nhif.client.NhifApiClient;
import com.oau.nhif.client.NhifApiClientFactory;
import com.oau.nhif.client.model.PackageItem;
import com.oau.nhif.client.model.ItemType;
import com.oau.nhif.client.model.BenefitScheme;
import com.oau.nhif.client.model.PricePackage;
import com.oau.nhif.exception.NhifApiException;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Example class demonstrating how to use the NHIF Packages API
 * This covers retrieving package items including:
 * - Medical services and procedures
 * - Drug packages and medications
 * - Service pricing and coverage information
 * - Restriction and eligibility details
 */
public class PackagesApiExample {
    
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
            
            System.out.println("=== NHIF Packages API Example ===\n");
            
            // Get price packages first to understand pricing structures
            System.out.println("1. Fetching price packages...");
            
            List<PricePackage> pricePackages = client.getPricePackages().get();
            System.out.println("Total price packages found: " + pricePackages.size());
            
            System.out.println("\nAvailable price packages:");
            pricePackages.stream()
                .limit(6)
                .forEach(pkg -> {
                    System.out.printf("  ID: %d - %s\n", 
                        pkg.getPackageID(), pkg.getPackageName());
                    System.out.printf("    Pricing Scheme: %d, Period: %d-%d\n", 
                        pkg.getPricingSchemeID(), pkg.getFromMonthSerial(), pkg.getToMonthSerial());
                    System.out.printf("    Alias: %s, Active: %s\n", 
                        pkg.getAlias(), pkg.getIsActive());
                });
            
            // Get benefit schemes to understand available schemes
            System.out.println("\n2. Fetching benefit schemes...");
            
            List<BenefitScheme> benefitSchemes = client.getBenefitSchemes().get();
            System.out.println("Total benefit schemes found: " + benefitSchemes.size());
            
            System.out.println("\nAvailable benefit schemes:");
            benefitSchemes.stream()
                .limit(8)
                .forEach(scheme -> {
                    System.out.printf("  ID: %d - %s\n", 
                        scheme.getSchemeID(), scheme.getSchemeName());
                    System.out.printf("    Created: %s by %s\n", 
                        scheme.getDateCreated(), scheme.getCreatedBy());
                });
            
            // Get all item types to understand the categories
            System.out.println("\n3. Fetching item types...");
            
            List<ItemType> itemTypes = client.getItemTypes().get();
            System.out.println("Total item types found: " + itemTypes.size());
            
            System.out.println("\nAvailable item types:");
            itemTypes.forEach(itemType -> {
                System.out.printf("  ID: %d - %s (%s)\n", 
                    itemType.getItemTypeID(), 
                    itemType.getTypeName(), 
                    itemType.getItemGroup());
                System.out.printf("    Alias: %s, Display: %s\n", 
                    itemType.getAlias(), 
                    itemType.getDisplayItem());
            });
            
            // Get all package items
            System.out.println("\n4. Fetching all package items...");
            
            List<PackageItem> packageItems = client.getPackageItems().get();
            System.out.println("Total package items found: " + packageItems.size());
            
            // Get facility-specific price package items
            System.out.println("\n5. Fetching facility-specific price package items...");
            
            List<PackageItem> facilityPackageItems = client.getPricePackageByFacility(clientId).get();
            System.out.println("Facility-specific package items found: " + facilityPackageItems.size());
            
            if (!facilityPackageItems.isEmpty()) {
                System.out.println("\nFirst 3 facility-specific items:");
                facilityPackageItems.stream()
                    .limit(3)
                    .forEach(item -> {
                        System.out.printf("  %s - %s\n", item.getItemCode(), item.getItemName());
                        System.out.printf("    Type: %d, Price: TSh %,.2f, Coverage: %d%%\n", 
                            item.getItemTypeID(), item.getCommonPrice(), item.getPercentCovered());
                        System.out.printf("    Restricted: %s, Active: %s\n", 
                            item.getIsRestricted(), item.getIsActive());
                    });
            }
            
            if (packageItems.isEmpty()) {
                System.out.println("No package items found.");
                return;
            }
            
            // Display first few items as examples
            System.out.println("\nFirst 5 package items:");
            packageItems.stream()
                .limit(5)
                .forEach(item -> {
                    System.out.printf("  Item Code: %s\n", item.getItemCode());
                    System.out.printf("  Item Name: %s\n", item.getItemName());
                    System.out.printf("  Item Type ID: %s\n", item.getItemTypeID());
                    System.out.printf("  Service Type ID: %s\n", item.getServiceTypeID());
                    System.out.printf("  Common Price: %s\n", item.getCommonPrice());
                    System.out.printf("  Percent Covered: %s%%\n", item.getPercentCovered());
                    System.out.printf("  Is Restricted: %s\n", item.getIsRestricted());
                    System.out.printf("  Is Active: %s\n", item.getIsActive());
                    System.out.println("  ---");
                });
            
            // Compare all items vs facility-specific items
            System.out.println("\n6. Comparing all items vs facility-specific items...");
            if (!facilityPackageItems.isEmpty()) {
                System.out.printf("All items: %d, Facility-specific: %d (%.1f%% coverage)\n", 
                    packageItems.size(), facilityPackageItems.size(), 
                    (facilityPackageItems.size() * 100.0 / packageItems.size()));
                
                // Compare by item type
                System.out.println("\nFacility items by type:");
                itemTypes.stream()
                    .limit(5)
                    .forEach(itemType -> {
                        long facilityCount = facilityPackageItems.stream()
                            .filter(item -> item.getItemTypeID() != null && 
                                          item.getItemTypeID().equals(itemType.getItemTypeID()))
                            .count();
                        long totalCount = packageItems.stream()
                            .filter(item -> item.getItemTypeID() != null && 
                                          item.getItemTypeID().equals(itemType.getItemTypeID()))
                            .count();
                        System.out.printf("  %s: %d/%d items (%.1f%%)\n", 
                            itemType.getTypeName(), facilityCount, totalCount,
                            totalCount > 0 ? (facilityCount * 100.0 / totalCount) : 0);
                    });
            }
            
            // Analyze items by type using the item types we fetched
            System.out.println("\n7. Analyzing all items by type...");
            itemTypes.stream()
                .limit(5)
                .forEach(itemType -> {
                    long count = packageItems.stream()
                        .filter(item -> item.getItemTypeID() != null && 
                                      item.getItemTypeID().equals(itemType.getItemTypeID()))
                        .count();
                    System.out.printf("  %s (%s): %d items\n", 
                        itemType.getTypeName(), itemType.getItemGroup(), count);
                });
            
            // Filter and display restricted items
            System.out.println("\n8. Analyzing restricted items...");
            List<PackageItem> restrictedItems = packageItems.stream()
                .filter(item -> Boolean.TRUE.equals(item.getIsRestricted()))
                .limit(3)
                .collect(Collectors.toList());
            
            System.out.println("Restricted items (first 3):");
            restrictedItems.forEach(item -> {
                System.out.printf("  %s - %s\n", item.getItemCode(), item.getItemName());
                System.out.printf("    Waiting Period: %s days\n", item.getWaitingPeriod());
                System.out.printf("    Eligibility: %s\n", item.getEligibility());
                if (item.getPractitionerQualifications() != null) {
                    System.out.printf("    Required Qualifications: %s\n", item.getPractitionerQualifications());
                }
                System.out.println();
            });
            
            // Filter and display active items with full coverage
            System.out.println("9. Active items with 100% coverage...");
            List<PackageItem> fullyCoveredItems = packageItems.stream()
                .filter(item -> Boolean.TRUE.equals(item.getIsActive()) && 
                              item.getPercentCovered() != null && 
                              item.getPercentCovered() == 100)
                .limit(5)
                .collect(Collectors.toList());
            
            System.out.println("Active items with 100% coverage (first 5):");
            fullyCoveredItems.forEach(item -> {
                System.out.printf("  %s - %s\n", item.getItemCode(), item.getItemName());
                System.out.printf("    Common Price: TSh %,.2f\n", item.getCommonPrice());
                System.out.printf("    Service Type ID: %s\n", item.getServiceTypeID());
                System.out.println();
            });
            
            // Filter items by service type (if available)
            System.out.println("10. Items by service type...");
            packageItems.stream()
                .filter(item -> item.getServiceTypeID() != null)
                .collect(java.util.stream.Collectors.groupingBy(
                    PackageItem::getServiceTypeID,
                    java.util.stream.Collectors.counting()))
                .entrySet().stream()
                .limit(5)
                .forEach(entry -> {
                    System.out.printf("  Service Type %d: %d items\n", 
                        entry.getKey(), entry.getValue());
                });
            
            // Display medication items (items with dosage information)
            System.out.println("\n11. Medication items (with dosage info)...");
            List<PackageItem> medications = packageItems.stream()
                .filter(item -> item.getDosage() != null && !item.getDosage().trim().isEmpty())
                .limit(3)
                .collect(Collectors.toList());
            
            medications.forEach(item -> {
                System.out.printf("  %s - %s\n", item.getItemCode(), item.getItemName());
                System.out.printf("    Strength: %s\n", item.getStrength());
                System.out.printf("    Dosage: %s\n", item.getDosage());
                System.out.printf("    Calculated Per Day: %s\n", item.getCalculatedPerDay());
                System.out.println();
            });
            
            // Display recent items (based on creation date)
            System.out.println("12. Recently created items...");
            List<PackageItem> recentItems = packageItems.stream()
                .filter(item -> item.getDateCreated() != null)
                .sorted((a, b) -> b.getDateCreated().compareTo(a.getDateCreated()))
                .limit(3)
                .collect(Collectors.toList());
            
            recentItems.forEach(item -> {
                System.out.printf("  %s - %s\n", item.getItemCode(), item.getItemName());
                System.out.printf("    Created: %s by %s\n", item.getDateCreated(), item.getCreatedBy());
                System.out.printf("    Last Modified: %s by %s\n", 
                    item.getLastModified(), item.getLastModifiedBy());
                System.out.println();
            });
            
            // Analyze price package patterns
            System.out.println("13. Price package analysis...");
            System.out.println("Currently active packages:");
            pricePackages.stream()
                .filter(pkg -> Boolean.TRUE.equals(pkg.getIsActive()))
                .forEach(pkg -> {
                    System.out.printf("  %d - %s (Scheme: %d)\n", 
                        pkg.getPackageID(), pkg.getPackageName(), pkg.getPricingSchemeID());
                });
            
            System.out.println("\nPackages by type:");
            System.out.println("Standard packages (100s): Standard Benefit Package");
            System.out.println("Corporate packages (200s+): Bunge, BoT, CRDB, NMB, DCB, WVT");
            System.out.println("Supplementary packages (400s-600s): Option 1, 2, 3");
            System.out.println("Government packages (1100s+): NSSF, TRA");
            
            // Analyze benefit scheme usage patterns
            System.out.println("\n14. Benefit scheme analysis...");
            System.out.println("Standard NHIF schemes (1000s):");
            benefitSchemes.stream()
                .filter(scheme -> scheme.getSchemeID() >= 1000 && scheme.getSchemeID() < 2000)
                .limit(5)
                .forEach(scheme -> {
                    System.out.printf("  %d - %s\n", scheme.getSchemeID(), scheme.getSchemeName());
                });
            
            System.out.println("\nCorporate schemes (2000s+):");
            benefitSchemes.stream()
                .filter(scheme -> scheme.getSchemeID() >= 2000)
                .limit(5)
                .forEach(scheme -> {
                    System.out.printf("  %d - %s\n", scheme.getSchemeID(), scheme.getSchemeName());
                });
            
            System.out.println("=== Packages API Example Complete ===");
            
        } catch (NhifApiException | InterruptedException | ExecutionException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Utility method to find specific package items by criteria
     */
    public static void findSpecificItems(NhifApiClient client) 
            throws InterruptedException, ExecutionException, NhifApiException {
        
        System.out.println("=== Finding Specific Package Items ===");
        
        List<PackageItem> allItems = client.getPackageItems().get();
        List<ItemType> itemTypes = client.getItemTypes().get();
        List<BenefitScheme> benefitSchemes = client.getBenefitSchemes().get();
        List<PricePackage> pricePackages = client.getPricePackages().get();
        
        // Find surgical procedures (typically have higher prices)
        System.out.println("High-value procedures (>1,000,000 TSh):");
        allItems.stream()
            .filter(item -> item.getCommonPrice() != null && item.getCommonPrice() > 1000000)
            .limit(5)
            .forEach(item -> {
                System.out.printf("  %s - %s (TSh %,.2f)\n", 
                    item.getItemCode(), item.getItemName(), item.getCommonPrice());
            });
        
        // Find items with specific item type and show type information
        System.out.println("\nSurgical items (Cardiac Services - Type ID 11):");
        ItemType cardiacType = itemTypes.stream()
            .filter(type -> type.getItemTypeID() == 11)
            .findFirst()
            .orElse(null);
            
        if (cardiacType != null) {
            System.out.printf("Item Type: %s (%s)\n", 
                cardiacType.getTypeName(), cardiacType.getItemGroup());
            
            allItems.stream()
                .filter(item -> item.getItemTypeID() != null && item.getItemTypeID() == 11)
                .limit(3)
                .forEach(item -> {
                    System.out.printf("  %s - %s\n", item.getItemCode(), item.getItemName());
                    System.out.printf("    Price: TSh %,.2f, Coverage: %d%%\n", 
                        item.getCommonPrice(), item.getPercentCovered());
                });
        }
        
        // Find items with waiting periods
        System.out.println("\nItems with waiting periods:");
        allItems.stream()
            .filter(item -> item.getWaitingPeriod() != null && item.getWaitingPeriod() > 0)
            .limit(3)
            .forEach(item -> {
                System.out.printf("  %s - %s (%d days waiting)\n", 
                    item.getItemCode(), item.getItemName(), item.getWaitingPeriod());
            });
    }
    
    /**
     * Utility method to analyze package item statistics
     */
    public static void analyzePackageStatistics(List<PackageItem> items) {
        System.out.println("=== Package Item Statistics ===");
        
        long totalItems = items.size();
        long activeItems = items.stream()
            .filter(item -> Boolean.TRUE.equals(item.getIsActive()))
            .count();
        long restrictedItems = items.stream()
            .filter(item -> Boolean.TRUE.equals(item.getIsRestricted()))
            .count();
        
        double averagePrice = items.stream()
            .filter(item -> item.getCommonPrice() != null && item.getCommonPrice() > 0)
            .mapToDouble(PackageItem::getCommonPrice)
            .average()
            .orElse(0.0);
        
        double averageCoverage = items.stream()
            .filter(item -> item.getPercentCovered() != null)
            .mapToInt(PackageItem::getPercentCovered)
            .average()
            .orElse(0.0);
        
        System.out.printf("Total Items: %d\n", totalItems);
        System.out.printf("Active Items: %d (%.1f%%)\n", activeItems, (activeItems * 100.0 / totalItems));
        System.out.printf("Restricted Items: %d (%.1f%%)\n", restrictedItems, (restrictedItems * 100.0 / totalItems));
        System.out.printf("Average Price: TSh %,.2f\n", averagePrice);
        System.out.printf("Average Coverage: %.1f%%\n", averageCoverage);
    }
    
    /**
     * Utility method to analyze benefit scheme statistics
     */
    public static void analyzeBenefitSchemes(List<BenefitScheme> schemes) {
        System.out.println("=== Benefit Scheme Analysis ===");
        
        long totalSchemes = schemes.size();
        
        // Group by scheme ID ranges
        long standardSchemes = schemes.stream()
            .filter(scheme -> scheme.getSchemeID() >= 1000 && scheme.getSchemeID() < 2000)
            .count();
        long corporateSchemes = schemes.stream()
            .filter(scheme -> scheme.getSchemeID() >= 2000)
            .count();
        
        // Find most recent scheme
        BenefitScheme mostRecent = schemes.stream()
            .filter(scheme -> scheme.getDateCreated() != null)
            .max((a, b) -> a.getDateCreated().compareTo(b.getDateCreated()))
            .orElse(null);
        
        System.out.printf("Total Schemes: %d\n", totalSchemes);
        System.out.printf("Standard NHIF Schemes: %d\n", standardSchemes);
        System.out.printf("Corporate/Special Schemes: %d\n", corporateSchemes);
        
        if (mostRecent != null) {
            System.out.printf("Most Recent Scheme: %s (ID: %d, Created: %s)\n", 
                mostRecent.getSchemeName(), mostRecent.getSchemeID(), mostRecent.getDateCreated());
        }
        
        // Show schemes by creation pattern
        System.out.println("\nScheme Categories:");
        System.out.println("- Wildlife/Tourism Schemes: Ngorongoro, Serengeti, Tarangire, Mikumi");
        System.out.println("- Mineral/Resource Schemes: Tanzanite");
        System.out.println("- Social Programs: Student, Toto Afya Kadi");
        System.out.println("- Financial/Investment: Najali, Wekeza, Timiza");
        System.out.println("- Government Institutions: NSSF, TRA");
        System.out.println("- Banking/Financial: BoT, CRDB, NMB, DCB, WVT");
    }
    
    /**
     * Utility method to analyze price package statistics
     */
    public static void analyzePricePackages(List<PricePackage> packages) {
        System.out.println("=== Price Package Analysis ===");
        
        long totalPackages = packages.size();
        long activePackages = packages.stream()
            .filter(pkg -> Boolean.TRUE.equals(pkg.getIsActive()))
            .count();
        
        // Group by package ID ranges
        long standardPackages = packages.stream()
            .filter(pkg -> pkg.getPackageID() >= 100 && pkg.getPackageID() < 200)
            .count();
        long corporatePackages = packages.stream()
            .filter(pkg -> pkg.getPackageID() >= 200 && pkg.getPackageID() < 1000)
            .count();
        long supplementaryPackages = packages.stream()
            .filter(pkg -> pkg.getPackageID() >= 400 && pkg.getPackageID() < 700)
            .count();
        long governmentPackages = packages.stream()
            .filter(pkg -> pkg.getPackageID() >= 1100)
            .count();
        
        // Find most recent package
        PricePackage mostRecent = packages.stream()
            .filter(pkg -> pkg.getDateCreated() != null)
            .max((a, b) -> a.getDateCreated().compareTo(b.getDateCreated()))
            .orElse(null);
        
        System.out.printf("Total Packages: %d\n", totalPackages);
        System.out.printf("Active Packages: %d (%.1f%%)\n", activePackages, (activePackages * 100.0 / totalPackages));
        System.out.printf("Standard Packages: %d\n", standardPackages);
        System.out.printf("Corporate Packages: %d\n", corporatePackages);
        System.out.printf("Supplementary Packages: %d\n", supplementaryPackages);
        System.out.printf("Government Packages: %d\n", governmentPackages);
        
        if (mostRecent != null) {
            System.out.printf("Most Recent Package: %s (ID: %d, Created: %s)\n", 
                mostRecent.getPackageName(), mostRecent.getPackageID(), mostRecent.getDateCreated());
        }
        
        // Show pricing scheme distribution
        System.out.println("\nPricing Schemes:");
        packages.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                PricePackage::getPricingSchemeID,
                java.util.stream.Collectors.counting()))
            .entrySet().stream()
            .forEach(entry -> {
                System.out.printf("  Scheme %d: %d packages\n", 
                    entry.getKey(), entry.getValue());
            });
    }
}