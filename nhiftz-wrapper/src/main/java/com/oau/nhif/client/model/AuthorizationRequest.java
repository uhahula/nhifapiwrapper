package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Request model for authorizing a card
 */
public class AuthorizationRequest {
    @JsonProperty("cardNumber")
    private String cardNumber;
    
    @JsonProperty("facilityCode")
    private String facilityCode;
    
    @JsonProperty("visitDate")
    private String visitDate;
    
    @JsonProperty("diagnosis")
    private String diagnosis;
    
    @JsonProperty("items")
    private List<AuthorizationItem> items;
    
    @JsonProperty("referralNumber")
    private String referralNumber;
    
    @JsonProperty("remarks")
    private String remarks;
    
    // Getters and setters
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    
    public String getFacilityCode() { return facilityCode; }
    public void setFacilityCode(String facilityCode) { this.facilityCode = facilityCode; }
    
    public String getVisitDate() { return visitDate; }
    public void setVisitDate(String visitDate) { this.visitDate = visitDate; }
    
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    
    public List<AuthorizationItem> getItems() { return items; }
    public void setItems(List<AuthorizationItem> items) { this.items = items; }
    
    public String getReferralNumber() { return referralNumber; }
    public void setReferralNumber(String referralNumber) { this.referralNumber = referralNumber; }
    
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    
    public static class AuthorizationItem {
        @JsonProperty("itemCode")
        private String itemCode;
        
        @JsonProperty("quantity")
        private int quantity;
        
        @JsonProperty("unitPrice")
        private double unitPrice;
        
        // Getters and setters
        public String getItemCode() { return itemCode; }
        public void setItemCode(String itemCode) { this.itemCode = itemCode; }
        
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        
        public double getUnitPrice() { return unitPrice; }
        public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    }
}
