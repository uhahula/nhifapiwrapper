package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MonthlyClaimSubmission {
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
    
    @JsonProperty("SubmissionRemarks")
    private String submissionRemarks;

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

    public String getSubmissionRemarks() {
        return submissionRemarks;
    }

    public void setSubmissionRemarks(String submissionRemarks) {
        this.submissionRemarks = submissionRemarks;
    }

    @Override
    public String toString() {
        return "MonthlyClaimSubmission{" +
                "facilityCode='" + facilityCode + '\'' +
                ", claimYear=" + claimYear +
                ", claimMonth=" + claimMonth +
                ", foliosSubmitted=" + foliosSubmitted +
                ", totalAmountClaimed=" + totalAmountClaimed +
                ", submissionRemarks='" + submissionRemarks + '\'' +
                '}';
    }
}