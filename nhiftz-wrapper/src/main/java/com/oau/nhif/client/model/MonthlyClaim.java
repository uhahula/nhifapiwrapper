package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Model for submitting a monthly claim
 */
public class MonthlyClaim {
    @JsonProperty("facilityCode")
    private String facilityCode;
    
    @JsonProperty("month")
    private int month;
    
    @JsonProperty("year")
    private int year;
    
    @JsonProperty("claims")
    private List<ClaimSummary> claims;
    
    @JsonProperty("preparedBy")
    private String preparedBy;
    
    @JsonProperty("preparedDate")
    private String preparedDate;
    
    @JsonProperty("authorizedBy")
    private String authorizedBy;
    
    @JsonProperty("authorizationDate")
    private String authorizationDate;
    
    // Getters and setters
    public String getFacilityCode() { return facilityCode; }
    public void setFacilityCode(String facilityCode) { this.facilityCode = facilityCode; }
    
    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }
    
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    
    public List<ClaimSummary> getClaims() { return claims; }
    public void setClaims(List<ClaimSummary> claims) { this.claims = claims; }
    
    public String getPreparedBy() { return preparedBy; }
    public void setPreparedBy(String preparedBy) { this.preparedBy = preparedBy; }
    
    public String getPreparedDate() { return preparedDate; }
    public void setPreparedDate(String preparedDate) { this.preparedDate = preparedDate; }
    
    public String getAuthorizedBy() { return authorizedBy; }
    public void setAuthorizedBy(String authorizedBy) { this.authorizedBy = authorizedBy; }
    
    public String getAuthorizationDate() { return authorizationDate; }
    public void setAuthorizationDate(String authorizationDate) { this.authorizationDate = authorizationDate; }
    
    public static class ClaimSummary {
        @JsonProperty("claimNumber")
        private String claimNumber;
        
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
        
        // Getters and setters
        public String getClaimNumber() { return claimNumber; }
        public void setClaimNumber(String claimNumber) { this.claimNumber = claimNumber; }
        
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
    }
}
