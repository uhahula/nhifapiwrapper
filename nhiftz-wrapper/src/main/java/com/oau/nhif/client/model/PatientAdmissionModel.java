package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PatientAdmissionModel {
    @JsonProperty("AuthorizationNo")
    private String authorizationNo;
    
    @JsonProperty("FullName")
    private String fullName;
    
    @JsonProperty("Gender")
    private String gender;
    
    @JsonProperty("DateOfBirth")
    private String dateOfBirth;
    
    @JsonProperty("AdmissionTypeID")
    private Integer admissionTypeID;
    
    @JsonProperty("WardTypeID")
    private Integer wardTypeID;
    
    @JsonProperty("RoomTypeID")
    private Integer roomTypeID;
    
    @JsonProperty("ChargesPerDay")
    private Double chargesPerDay;
    
    @JsonProperty("PractitionerNo")
    private String practitionerNo;
    
    @JsonProperty("DiagnosisAtAdmission")
    private String diagnosisAtAdmission;
    
    @JsonProperty("PractitionersRemarks")
    private String practitionersRemarks;
    
    @JsonProperty("DateAdmitted")
    private String dateAdmitted;
    
    @JsonProperty("CreatedBy")
    private String createdBy;

    public String getAuthorizationNo() {
        return authorizationNo;
    }

    public void setAuthorizationNo(String authorizationNo) {
        this.authorizationNo = authorizationNo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getAdmissionTypeID() {
        return admissionTypeID;
    }

    public void setAdmissionTypeID(Integer admissionTypeID) {
        this.admissionTypeID = admissionTypeID;
    }

    public Integer getWardTypeID() {
        return wardTypeID;
    }

    public void setWardTypeID(Integer wardTypeID) {
        this.wardTypeID = wardTypeID;
    }

    public Integer getRoomTypeID() {
        return roomTypeID;
    }

    public void setRoomTypeID(Integer roomTypeID) {
        this.roomTypeID = roomTypeID;
    }

    public Double getChargesPerDay() {
        return chargesPerDay;
    }

    public void setChargesPerDay(Double chargesPerDay) {
        this.chargesPerDay = chargesPerDay;
    }

    public String getPractitionerNo() {
        return practitionerNo;
    }

    public void setPractitionerNo(String practitionerNo) {
        this.practitionerNo = practitionerNo;
    }

    public String getDiagnosisAtAdmission() {
        return diagnosisAtAdmission;
    }

    public void setDiagnosisAtAdmission(String diagnosisAtAdmission) {
        this.diagnosisAtAdmission = diagnosisAtAdmission;
    }

    public String getPractitionersRemarks() {
        return practitionersRemarks;
    }

    public void setPractitionersRemarks(String practitionersRemarks) {
        this.practitionersRemarks = practitionersRemarks;
    }

    public String getDateAdmitted() {
        return dateAdmitted;
    }

    public void setDateAdmitted(String dateAdmitted) {
        this.dateAdmitted = dateAdmitted;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}