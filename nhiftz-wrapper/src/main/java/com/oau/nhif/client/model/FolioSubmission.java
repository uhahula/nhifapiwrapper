package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Model for submitting a folio claim
 */
public class FolioSubmission {
    @JsonProperty("facilityCode")
    private String facilityCode;
    
    @JsonProperty("visitDate")
    private String visitDate;
    
    @JsonProperty("cardNumber")
    private String cardNumber;
    
    @JsonProperty("memberNumber")
    private String memberNumber;
    
    @JsonProperty("patientName")
    private String patientName;
    
    @JsonProperty("gender")
    private String gender;
    
    @JsonProperty("dateOfBirth")
    private String dateOfBirth;
    
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    
    @JsonProperty("diagnosis")
    private String diagnosis;
    
    @JsonProperty("referralNumber")
    private String referralNumber;
    
    @JsonProperty("items")
    private List<ClaimItem> items;
    
    @JsonProperty("doctorName")
    private String doctorName;
    
    @JsonProperty("doctorRegNumber")
    private String doctorRegNumber;
    
    @JsonProperty("remarks")
    private String remarks;
    
    // Getters and setters
    public String getFacilityCode() { return facilityCode; }
    public void setFacilityCode(String facilityCode) { this.facilityCode = facilityCode; }
    
    public String getVisitDate() { return visitDate; }
    public void setVisitDate(String visitDate) { this.visitDate = visitDate; }
    
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    
    public String getMemberNumber() { return memberNumber; }
    public void setMemberNumber(String memberNumber) { this.memberNumber = memberNumber; }
    
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    
    public String getReferralNumber() { return referralNumber; }
    public void setReferralNumber(String referralNumber) { this.referralNumber = referralNumber; }
    
    public List<ClaimItem> getItems() { return items; }
    public void setItems(List<ClaimItem> items) { this.items = items; }
    
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    
    public String getDoctorRegNumber() { return doctorRegNumber; }
    public void setDoctorRegNumber(String doctorRegNumber) { this.doctorRegNumber = doctorRegNumber; }
    
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    
    @Setter
    @Getter
    public static class ClaimItem {
        // Getters and setters
        @JsonProperty("itemCode")
        private String itemCode;
        
        @JsonProperty("description")
        private String description;
        
        @JsonProperty("quantity")
        private double quantity;
        
        @JsonProperty("unitPrice")
        private double unitPrice;
        
        @JsonProperty("totalPrice")
        private double totalPrice;

    }
}
