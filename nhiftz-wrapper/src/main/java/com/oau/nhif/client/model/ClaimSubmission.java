package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClaimSubmission {
    @JsonProperty("SubmissionID")
    private String submissionID;
    
    @JsonProperty("SubmissionNo")
    private String submissionNo;
    
    @JsonProperty("DateSubmitted")
    private String dateSubmitted;
    
    @JsonProperty("FacilityCode")
    private String facilityCode;
    
    @JsonProperty("ClaimYear")
    private Integer claimYear;
    
    @JsonProperty("ClaimMonth")
    private Integer claimMonth;
    
    @JsonProperty("FolioNo")
    private Integer folioNo;
    
    @JsonProperty("BillNo")
    private String billNo;
    
    @JsonProperty("SubmittedBy")
    private String submittedBy;
    
    @JsonProperty("CardNo")
    private String cardNo;
    
    @JsonProperty("AuthorizationNo")
    private String authorizationNo;
    
    @JsonProperty("VisitTypeID")
    private Integer visitTypeID;
    
    @JsonProperty("SchemeID")
    private Integer schemeID;
    
    @JsonProperty("AmountClaimed")
    private String amountClaimed;
    
    @JsonProperty("Remarks")
    private String remarks;
    
    @JsonProperty("SubmissionChannel")
    private String submissionChannel;
    
    @JsonProperty("HashCode")
    private String hashCode;

    public String getSubmissionID() {
        return submissionID;
    }

    public void setSubmissionID(String submissionID) {
        this.submissionID = submissionID;
    }

    public String getSubmissionNo() {
        return submissionNo;
    }

    public void setSubmissionNo(String submissionNo) {
        this.submissionNo = submissionNo;
    }

    public String getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(String dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public String getFacilityCode() {
        return facilityCode;
    }

    public void setFacilityCode(String facilityCode) {
        this.facilityCode = facilityCode;
    }

    public Integer getClaimYear() {
        return claimYear;
    }

    public void setClaimYear(Integer claimYear) {
        this.claimYear = claimYear;
    }

    public Integer getClaimMonth() {
        return claimMonth;
    }

    public void setClaimMonth(Integer claimMonth) {
        this.claimMonth = claimMonth;
    }

    public Integer getFolioNo() {
        return folioNo;
    }

    public void setFolioNo(Integer folioNo) {
        this.folioNo = folioNo;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getAuthorizationNo() {
        return authorizationNo;
    }

    public void setAuthorizationNo(String authorizationNo) {
        this.authorizationNo = authorizationNo;
    }

    public Integer getVisitTypeID() {
        return visitTypeID;
    }

    public void setVisitTypeID(Integer visitTypeID) {
        this.visitTypeID = visitTypeID;
    }

    public Integer getSchemeID() {
        return schemeID;
    }

    public void setSchemeID(Integer schemeID) {
        this.schemeID = schemeID;
    }

    public String getAmountClaimed() {
        return amountClaimed;
    }

    public void setAmountClaimed(String amountClaimed) {
        this.amountClaimed = amountClaimed;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSubmissionChannel() {
        return submissionChannel;
    }

    public void setSubmissionChannel(String submissionChannel) {
        this.submissionChannel = submissionChannel;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    @Override
    public String toString() {
        return "ClaimSubmission{" +
                "submissionID='" + submissionID + '\'' +
                ", submissionNo='" + submissionNo + '\'' +
                ", dateSubmitted='" + dateSubmitted + '\'' +
                ", facilityCode='" + facilityCode + '\'' +
                ", claimYear=" + claimYear +
                ", claimMonth=" + claimMonth +
                ", folioNo=" + folioNo +
                ", billNo='" + billNo + '\'' +
                ", submittedBy='" + submittedBy + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", authorizationNo='" + authorizationNo + '\'' +
                ", visitTypeID=" + visitTypeID +
                ", schemeID=" + schemeID +
                ", amountClaimed='" + amountClaimed + '\'' +
                ", remarks='" + remarks + '\'' +
                ", submissionChannel='" + submissionChannel + '\'' +
                ", hashCode='" + hashCode + '\'' +
                '}';
    }
}