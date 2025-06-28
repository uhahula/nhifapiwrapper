package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response model for card authorization
 */
public class AuthorizationResponse {
    @JsonProperty("success")
    private boolean success;
    
    @JsonProperty("authorizationNumber")
    private String authorizationNumber;
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("approvedAmount")
    private double approvedAmount;
    
    @JsonProperty("expiryDate")
    private String expiryDate;
    
    // Getters and setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getAuthorizationNumber() { return authorizationNumber; }
    public void setAuthorizationNumber(String authorizationNumber) { this.authorizationNumber = authorizationNumber; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public double getApprovedAmount() { return approvedAmount; }
    public void setApprovedAmount(double approvedAmount) { this.approvedAmount = approvedAmount; }
    
    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
}
