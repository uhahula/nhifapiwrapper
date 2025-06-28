package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response model for monthly claim submission
 */
public class MonthlyClaimResponse {
    @JsonProperty("success")
    private boolean success;
    
    @JsonProperty("batchNumber")
    private String batchNumber;
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("totalClaims")
    private int totalClaims;
    
    @JsonProperty("totalAmount")
    private double totalAmount;
    
    @JsonProperty("submissionDate")
    private String submissionDate;
    
    @JsonProperty("referenceNumber")
    private String referenceNumber;
    
    // Getters and setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getBatchNumber() { return batchNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public int getTotalClaims() { return totalClaims; }
    public void setTotalClaims(int totalClaims) { this.totalClaims = totalClaims; }
    
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
    public String getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(String submissionDate) { this.submissionDate = submissionDate; }
    
    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
}
