package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MonthlyClaimSubmissionResponse {
    // Success response fields
    @JsonProperty("AcknowledgementNo")
    private String acknowledgementNo;
    
    @JsonProperty("FacilityCode")
    private String facilityCode;
    
    @JsonProperty("ClaimYear")
    private Integer claimYear;
    
    @JsonProperty("ClaimMonth")
    private Integer claimMonth;
    
    @JsonProperty("FoliosSubmitted")
    private Integer foliosSubmitted;
    
    @JsonProperty("TotalAmountClaimed")
    private Double totalAmountClaimed;
    
    @JsonProperty("SubmittedBy")
    private String submittedBy;
    
    @JsonProperty("DateSubmitted")
    private String dateSubmitted;
    
    // Error response fields
    @JsonProperty("ErrorMessage")
    private String errorMessage;
    
    @JsonProperty("StatusCode")
    private Integer statusCode;

    public String getAcknowledgementNo() {
        return acknowledgementNo;
    }

    public void setAcknowledgementNo(String acknowledgementNo) {
        this.acknowledgementNo = acknowledgementNo;
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

    public Integer getFoliosSubmitted() {
        return foliosSubmitted;
    }

    public void setFoliosSubmitted(Integer foliosSubmitted) {
        this.foliosSubmitted = foliosSubmitted;
    }

    public Double getTotalAmountClaimed() {
        return totalAmountClaimed;
    }

    public void setTotalAmountClaimed(Double totalAmountClaimed) {
        this.totalAmountClaimed = totalAmountClaimed;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    public String getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(String dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isSuccess() {
        return errorMessage == null && statusCode == null;
    }

    @Override
    public String toString() {
        if (isSuccess()) {
            return "MonthlyClaimSubmissionResponse{" +
                    "acknowledgementNo='" + acknowledgementNo + '\'' +
                    ", facilityCode='" + facilityCode + '\'' +
                    ", claimYear=" + claimYear +
                    ", claimMonth=" + claimMonth +
                    ", foliosSubmitted=" + foliosSubmitted +
                    ", totalAmountClaimed=" + totalAmountClaimed +
                    ", submittedBy='" + submittedBy + '\'' +
                    ", dateSubmitted='" + dateSubmitted + '\'' +
                    '}';
        } else {
            return "MonthlyClaimSubmissionResponse{" +
                    "errorMessage='" + errorMessage + '\'' +
                    ", statusCode=" + statusCode +
                    '}';
        }
    }
}