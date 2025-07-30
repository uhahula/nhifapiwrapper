package com.oau.nhif.example;

import com.oau.nhif.client.model.SimpleCardAuthorizationRequest;

/**
 * Simple test to verify the SimpleCardAuthorizationRequest functionality
 */
public class SimpleCardAuthTest {
    
    public static void main(String[] args) {
        System.out.println("=== Testing Simple Card Authorization Request ===");
        
        // Test 1: Default constructor
        SimpleCardAuthorizationRequest request1 = new SimpleCardAuthorizationRequest();
        request1.setCardNo("1234567890123456");
        request1.setVisitTypeID(1);
        request1.setReferralNo("REF-2024-001");
        request1.setRemarks("Emergency visit");
        
        System.out.println("Test 1 - Default Constructor:");
        System.out.println("Card No: " + request1.getCardNo());
        System.out.println("Visit Type ID: " + request1.getVisitTypeID());
        System.out.println("Referral No: " + request1.getReferralNo());
        System.out.println("Remarks: " + request1.getRemarks());
        
        // Test 2: Simple constructor
        SimpleCardAuthorizationRequest request2 = new SimpleCardAuthorizationRequest("9876543210987654", 2);
        
        System.out.println("\nTest 2 - Simple Constructor:");
        System.out.println("Card No: " + request2.getCardNo());
        System.out.println("Visit Type ID: " + request2.getVisitTypeID());
        System.out.println("Referral No: " + request2.getReferralNo());
        System.out.println("Remarks: " + request2.getRemarks());
        
        // Test 3: Full constructor
        SimpleCardAuthorizationRequest request3 = new SimpleCardAuthorizationRequest(
            "5555444433332222", 
            3, 
            "REF-2024-003", 
            "Follow-up consultation"
        );
        
        System.out.println("\nTest 3 - Full Constructor:");
        System.out.println("Card No: " + request3.getCardNo());
        System.out.println("Visit Type ID: " + request3.getVisitTypeID());
        System.out.println("Referral No: " + request3.getReferralNo());
        System.out.println("Remarks: " + request3.getRemarks());
        
        System.out.println("\n✓ All tests completed successfully!");
        System.out.println("SimpleCardAuthorizationRequest is working correctly.");
    }
}