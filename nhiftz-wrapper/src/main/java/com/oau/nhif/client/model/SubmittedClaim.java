package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Model representing a submitted claim
 */
public class SubmittedClaim {
    @JsonProperty("claimNumber")
    private String claimNumber;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("submissionDate")
    private String submissionDate;
    
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
    
    @JsonProperty("diagnosis")
    private String diagnosis;
    
    @JsonProperty("amountClaimed")
    private double amountClaimed;
    
    @JsonProperty("amountApproved")
    private Double amountApproved;
    
    @JsonProperty("rejectionReason")
    private String rejectionReason;
    
    @JsonProperty("items")
    private List<ClaimItem> items;
    
    @JsonProperty("processingDate")
    private String processingDate;
    
    // Getters and setters
    public String getClaimNumber() { return claimNumber; }
    public void setClaimNumber(String claimNumber) { this.claimNumber = claimNumber; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(String submissionDate) { this.submissionDate = submissionDate; }
    
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
    
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    
    public double getAmountClaimed() { return amountClaimed; }
    public void setAmountClaimed(double amountClaimed) { this.amountClaimed = amountClaimed; }
    
    public Double getAmountApproved() { return amountApproved; }
    public void setAmountApproved(Double amountApproved) { this.amountApproved = amountApproved; }
    
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    
    public List<ClaimItem> getItems() { return items; }
    public void setItems(List<ClaimItem> items) { this.items = items; }
    
    public String getProcessingDate() { return processingDate; }
    public void setProcessingDate(String processingDate) { this.processingDate = processingDate; }
    
    public static class ClaimItem {
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
        
        @JsonProperty("status")
        private String status;
        
        @JsonProperty("approvedAmount")
        private Double approvedAmount;
        
        @JsonProperty("rejectionReason")
        private String rejectionReason;
        
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
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public Double getApprovedAmount() { return approvedAmount; }
        public void setApprovedAmount(Double approvedAmount) { this.approvedAmount = approvedAmount; }
        
        public String getRejectionReason() { return rejectionReason; }
        public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    }
}
