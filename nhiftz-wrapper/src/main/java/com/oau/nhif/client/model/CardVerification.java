package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response model for card verification
 */
public class CardVerification {
    @JsonProperty("cardNumber")
    private String cardNumber;
    
    @JsonProperty("isValid")
    private boolean isValid;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("memberName")
    private String memberName;
    
    @JsonProperty("memberNumber")
    private String memberNumber;
    
    @JsonProperty("cardExpiryDate")
    private String cardExpiryDate;
    
    // Getters and setters
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    
    public boolean isValid() { return isValid; }
    public void setValid(boolean valid) { isValid = valid; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }
    
    public String getMemberNumber() { return memberNumber; }
    public void setMemberNumber(String memberNumber) { this.memberNumber = memberNumber; }
    
    public String getCardExpiryDate() { return cardExpiryDate; }
    public void setCardExpiryDate(String cardExpiryDate) { this.cardExpiryDate = cardExpiryDate; }
}
