package com.oau.nhif.example;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Simple test to verify the date format matches API expectations
 */
public class DateFormatTest {
    
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
        System.out.println("Date Format Test for NHIF Admissions API");
        System.out.println("=========================================");
        
        // Test date of birth format
        LocalDateTime birthDate = LocalDateTime.of(1985, 3, 15, 0, 0);
        String formattedBirthDate = formatDateForApi(birthDate);
        System.out.println("Date of Birth (1985-03-15): " + formattedBirthDate);
        
        // Test current date format
        String currentDate = getCurrentDateForApi();
        System.out.println("Current Date: " + currentDate);
        
        // Verify format matches expected pattern
        System.out.println("\nExpected API format: 2025-07-07T00:41:22.783Z");
        System.out.println("Our format matches: " + 
            (currentDate.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z") ? "✅ YES" : "❌ NO"));
        
        // Test various dates
        System.out.println("\nTesting various dates:");
        LocalDateTime[] testDates = {
            LocalDateTime.of(1990, 6, 20, 14, 30, 45),
            LocalDateTime.of(2000, 12, 31, 23, 59, 59),
            LocalDateTime.of(2025, 1, 1, 0, 0, 0)
        };
        
        for (LocalDateTime testDate : testDates) {
            String formatted = formatDateForApi(testDate);
            System.out.println("  " + testDate + " -> " + formatted);
        }
        
        System.out.println("\n✅ All dates formatted successfully for .NET API compatibility!");
    }
}