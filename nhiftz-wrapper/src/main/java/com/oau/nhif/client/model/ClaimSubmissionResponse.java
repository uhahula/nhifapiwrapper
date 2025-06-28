package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response model for claim submission
 */
public class ClaimSubmissionResponse {
    @JsonProperty("success")
    private boolean success;
    
    @JsonProperty("claimNumber")
    private String claimNumber;
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("timestamp")
    private String timestamp;
    
    @JsonProperty("amountClaimed")
    private double amountClaimed;
    
    @JsonProperty("referenceNumber")
    private String referenceNumber;
    
    // Getters and setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getClaimNumber() { return claimNumber; }
    public void setClaimNumber(String claimNumber) { this.claimNumber = claimNumber; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    
    public double getAmountClaimed() { return amountClaimed; }
    public void setAmountClaimed(double amountClaimed) { this.amountClaimed = amountClaimed; }
    
    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
}
