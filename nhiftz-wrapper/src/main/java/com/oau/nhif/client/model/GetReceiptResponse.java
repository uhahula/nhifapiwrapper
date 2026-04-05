package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Model representing the response from the GetReceipt API
 */
public class GetReceiptResponse {
    @JsonProperty("ReceiptNo")
    private String receiptNo;
    
    @JsonProperty("FacilityCode")
    private String facilityCode;
    
    @JsonProperty("FacilityName")
    private String facilityName;
    
    @JsonProperty("ClaimYear")
    private int claimYear;
    
    @JsonProperty("ClaimMonth")
    private int claimMonth;
    
    @JsonProperty("CardNo")
    private String cardNo;
    
    @JsonProperty("FirstName")
    private String firstName;
    
    @JsonProperty("LastName")
    private String lastName;
    
    @JsonProperty("Gender")
    private String gender;
    
    @JsonProperty("TelephoneNo")
    private String telephoneNo;
    
    @JsonProperty("PatientFileNo")
    private String patientFileNo;
    
    @JsonProperty("AuthorizationNo")
    private String authorizationNo;
    
    @JsonProperty("AmountClaimed")
    private double amountClaimed;
    
    @JsonProperty("ReceiptItems")
    private List<ReceiptItem> receiptItems;
    
    // Getters and setters
    public String getReceiptNo() { return receiptNo; }
    public void setReceiptNo(String receiptNo) { this.receiptNo = receiptNo; }
    
    public String getFacilityCode() { return facilityCode; }
    public void setFacilityCode(String facilityCode) { this.facilityCode = facilityCode; }
    
    public String getFacilityName() { return facilityName; }
    public void setFacilityName(String facilityName) { this.facilityName = facilityName; }
    
    public int getClaimYear() { return claimYear; }
    public void setClaimYear(int claimYear) { this.claimYear = claimYear; }
    
    public int getClaimMonth() { return claimMonth; }
    public void setClaimMonth(int claimMonth) { this.claimMonth = claimMonth; }
    
    public String getCardNo() { return cardNo; }
    public void setCardNo(String cardNo) { this.cardNo = cardNo; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getTelephoneNo() { return telephoneNo; }
    public void setTelephoneNo(String telephoneNo) { this.telephoneNo = telephoneNo; }
    
    public String getPatientFileNo() { return patientFileNo; }
    public void setPatientFileNo(String patientFileNo) { this.patientFileNo = patientFileNo; }
    
    public String getAuthorizationNo() { return authorizationNo; }
    public void setAuthorizationNo(String authorizationNo) { this.authorizationNo = authorizationNo; }
    
    public double getAmountClaimed() { return amountClaimed; }
    public void setAmountClaimed(double amountClaimed) { this.amountClaimed = amountClaimed; }
    
    public List<ReceiptItem> getReceiptItems() { return receiptItems; }
    public void setReceiptItems(List<ReceiptItem> receiptItems) { this.receiptItems = receiptItems; }
    
    /**
     * Nested class representing individual receipt items
     */
    public static class ReceiptItem {
        @JsonProperty("ItemCode")
        private String itemCode;
        
        @JsonProperty("ItemName")
        private String itemName;
        
        @JsonProperty("UnitPrice")
        private double unitPrice;
        
        @JsonProperty("ItemQuantity")
        private int itemQuantity;
        
        @JsonProperty("AmountClaimed")
        private double amountClaimed;
        
        @JsonProperty("OtherDetails")
        private String otherDetails;
        
        // Getters and setters
        public String getItemCode() { return itemCode; }
        public void setItemCode(String itemCode) { this.itemCode = itemCode; }
        
        public String getItemName() { return itemName; }
        public void setItemName(String itemName) { this.itemName = itemName; }
        
        public double getUnitPrice() { return unitPrice; }
        public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
        
        public int getItemQuantity() { return itemQuantity; }
        public void setItemQuantity(int itemQuantity) { this.itemQuantity = itemQuantity; }
        
        public double getAmountClaimed() { return amountClaimed; }
        public void setAmountClaimed(double amountClaimed) { this.amountClaimed = amountClaimed; }
        
        public String getOtherDetails() { return otherDetails; }
        public void setOtherDetails(String otherDetails) { this.otherDetails = otherDetails; }
    }
}