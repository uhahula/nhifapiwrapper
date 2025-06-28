package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Model representing a claim receipt
 */
public class Receipt {
    @JsonProperty("receiptNumber")
    private String receiptNumber;
    
    @JsonProperty("receiptDate")
    private String receiptDate;
    
    @JsonProperty("claimNumber")
    private String claimNumber;
    
    @JsonProperty("facilityCode")
    private String facilityCode;
    
    @JsonProperty("facilityName")
    private String facilityName;
    
    @JsonProperty("cardNumber")
    private String cardNumber;
    
    @JsonProperty("memberNumber")
    private String memberNumber;
    
    @JsonProperty("patientName")
    private String patientName;
    
    @JsonProperty("visitDate")
    private String visitDate;
    
    @JsonProperty("amountApproved")
    private double amountApproved;
    
    @JsonProperty("paymentDate")
    private String paymentDate;
    
    @JsonProperty("paymentReference")
    private String paymentReference;
    
    @JsonProperty("items")
    private List<ReceiptItem> items;
    
    // Getters and setters
    public String getReceiptNumber() { return receiptNumber; }
    public void setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; }
    
    public String getReceiptDate() { return receiptDate; }
    public void setReceiptDate(String receiptDate) { this.receiptDate = receiptDate; }
    
    public String getClaimNumber() { return claimNumber; }
    public void setClaimNumber(String claimNumber) { this.claimNumber = claimNumber; }
    
    public String getFacilityCode() { return facilityCode; }
    public void setFacilityCode(String facilityCode) { this.facilityCode = facilityCode; }
    
    public String getFacilityName() { return facilityName; }
    public void setFacilityName(String facilityName) { this.facilityName = facilityName; }
    
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    
    public String getMemberNumber() { return memberNumber; }
    public void setMemberNumber(String memberNumber) { this.memberNumber = memberNumber; }
    
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    
    public String getVisitDate() { return visitDate; }
    public void setVisitDate(String visitDate) { this.visitDate = visitDate; }
    
    public double getAmountApproved() { return amountApproved; }
    public void setAmountApproved(double amountApproved) { this.amountApproved = amountApproved; }
    
    public String getPaymentDate() { return paymentDate; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }
    
    public String getPaymentReference() { return paymentReference; }
    public void setPaymentReference(String paymentReference) { this.paymentReference = paymentReference; }
    
    public List<ReceiptItem> getItems() { return items; }
    public void setItems(List<ReceiptItem> items) { this.items = items; }
    
    public static class ReceiptItem {
        @JsonProperty("itemCode")
        private String itemCode;
        
        @JsonProperty("description")
        private String description;
        
        @JsonProperty("quantity")
        private double quantity;
        
        @JsonProperty("unitPrice")
        private double unitPrice;
        
        @JsonProperty("totalPrice")
        private double totalPrice;
        
        @JsonProperty("approvedAmount")
        private double approvedAmount;
        
        // Getters and setters
        public String getItemCode() { return itemCode; }
        public void setItemCode(String itemCode) { this.itemCode = itemCode; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public double getQuantity() { return quantity; }
        public void setQuantity(double quantity) { this.quantity = quantity; }
        
        public double getUnitPrice() { return unitPrice; }
        public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
        
        public double getTotalPrice() { return totalPrice; }
        public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
        
        public double getApprovedAmount() { return approvedAmount; }
        public void setApprovedAmount(double approvedAmount) { this.approvedAmount = approvedAmount; }
    }
}
