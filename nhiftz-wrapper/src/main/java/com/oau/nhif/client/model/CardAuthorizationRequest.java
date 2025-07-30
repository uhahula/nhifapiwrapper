package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request model for card authorization via biometric verification
 */
public class CardAuthorizationRequest {
    @JsonProperty("cardNo")
    private String cardNo;
    
    @JsonProperty("biometricMethod")
    private String biometricMethod;
    
    @JsonProperty("nationalID")
    private String nationalID;
    
    @JsonProperty("fpCode")
    private String fpCode;
    
    @JsonProperty("imageData")
    private String imageData;
    
    @JsonProperty("visitTypeID")
    private int visitTypeID;
    
    @JsonProperty("referralNo")
    private String referralNo;
    
    @JsonProperty("remarks")
    private String remarks;
    
    // Constructors
    public CardAuthorizationRequest() {}
    
    public CardAuthorizationRequest(String cardNo, String biometricMethod, String nationalID, 
                                  String fpCode, String imageData, int visitTypeID, 
                                  String referralNo, String remarks) {
        this.cardNo = cardNo;
        this.biometricMethod = biometricMethod;
        this.nationalID = nationalID;
        this.fpCode = fpCode;
        this.imageData = imageData;
        this.visitTypeID = visitTypeID;
        this.referralNo = referralNo;
        this.remarks = remarks;
    }
    
    // Builder-style constructor for convenience
    public static CardAuthorizationRequest builder() {
        return new CardAuthorizationRequest();
    }
    
    public CardAuthorizationRequest cardNo(String cardNo) {
        this.cardNo = cardNo;
        return this;
    }
    
    public CardAuthorizationRequest biometricMethod(String biometricMethod) {
        this.biometricMethod = biometricMethod;
        return this;
    }
    
    public CardAuthorizationRequest nationalID(String nationalID) {
        this.nationalID = nationalID;
        return this;
    }
    
    public CardAuthorizationRequest fpCode(String fpCode) {
        this.fpCode = fpCode;
        return this;
    }
    
    public CardAuthorizationRequest imageData(String imageData) {
        this.imageData = imageData;
        return this;
    }
    
    public CardAuthorizationRequest visitTypeID(int visitTypeID) {
        this.visitTypeID = visitTypeID;
        return this;
    }
    
    public CardAuthorizationRequest referralNo(String referralNo) {
        this.referralNo = referralNo;
        return this;
    }
    
    public CardAuthorizationRequest remarks(String remarks) {
        this.remarks = remarks;
        return this;
    }
    
    // Getters and setters
    public String getCardNo() { return cardNo; }
    public void setCardNo(String cardNo) { this.cardNo = cardNo; }
    
    public String getBiometricMethod() { return biometricMethod; }
    public void setBiometricMethod(String biometricMethod) { this.biometricMethod = biometricMethod; }
    
    public String getNationalID() { return nationalID; }
    public void setNationalID(String nationalID) { this.nationalID = nationalID; }
    
    public String getFpCode() { return fpCode; }
    public void setFpCode(String fpCode) { this.fpCode = fpCode; }
    
    public String getImageData() { return imageData; }
    public void setImageData(String imageData) { this.imageData = imageData; }
    
    public int getVisitTypeID() { return visitTypeID; }
    public void setVisitTypeID(int visitTypeID) { this.visitTypeID = visitTypeID; }
    
    public String getReferralNo() { return referralNo; }
    public void setReferralNo(String referralNo) { this.referralNo = referralNo; }
    
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}