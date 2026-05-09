package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request model for {@code /api/Verification/VerifyCard}.
 *
 * NHIF now uses VerifyCard as the authorize endpoint (replacing the older
 * AuthorizeCard path). Fields below mirror the payload NHIF documents.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerifyCardRequest {
    @JsonProperty("cardNo")
    private String cardNo;

    @JsonProperty("verifierID")
    private Integer verifierID;

    @JsonProperty("cardTypeID")
    private Integer cardTypeID;

    @JsonProperty("biometricMethod")
    private String biometricMethod;

    @JsonProperty("fpCode")
    private String fpCode;

    @JsonProperty("imageData")
    private String imageData;

    @JsonProperty("visitTypeID")
    private Integer visitTypeID;

    @JsonProperty("referralNo")
    private String referralNo;

    @JsonProperty("remarks")
    private String remarks;

    @JsonProperty("wcfNotificationNo")
    private String wcfNotificationNo;

    @JsonProperty("mobileNo")
    private String mobileNo;

    @JsonProperty("facilityCode")
    private String facilityCode;

    public VerifyCardRequest() {}

    public VerifyCardRequest(String cardNo, Integer visitTypeID) {
        this.cardNo = cardNo;
        this.visitTypeID = visitTypeID;
    }

    public String getCardNo() { return cardNo; }
    public void setCardNo(String cardNo) { this.cardNo = cardNo; }

    public Integer getVerifierID() { return verifierID; }
    public void setVerifierID(Integer verifierID) { this.verifierID = verifierID; }

    public Integer getCardTypeID() { return cardTypeID; }
    public void setCardTypeID(Integer cardTypeID) { this.cardTypeID = cardTypeID; }

    public String getBiometricMethod() { return biometricMethod; }
    public void setBiometricMethod(String biometricMethod) { this.biometricMethod = biometricMethod; }

    public String getFpCode() { return fpCode; }
    public void setFpCode(String fpCode) { this.fpCode = fpCode; }

    public String getImageData() { return imageData; }
    public void setImageData(String imageData) { this.imageData = imageData; }

    public Integer getVisitTypeID() { return visitTypeID; }
    public void setVisitTypeID(Integer visitTypeID) { this.visitTypeID = visitTypeID; }

    public String getReferralNo() { return referralNo; }
    public void setReferralNo(String referralNo) { this.referralNo = referralNo; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getWcfNotificationNo() { return wcfNotificationNo; }
    public void setWcfNotificationNo(String wcfNotificationNo) { this.wcfNotificationNo = wcfNotificationNo; }

    public String getMobileNo() { return mobileNo; }
    public void setMobileNo(String mobileNo) { this.mobileNo = mobileNo; }

    public String getFacilityCode() { return facilityCode; }
    public void setFacilityCode(String facilityCode) { this.facilityCode = facilityCode; }
}
