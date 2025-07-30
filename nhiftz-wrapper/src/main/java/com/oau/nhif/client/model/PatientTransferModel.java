package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PatientTransferModel {
    @JsonProperty("AdmissionNo")
    private String admissionNo;
    
    @JsonProperty("PractitionerNo")
    private String practitionerNo;
    
    @JsonProperty("PractitionersRemarks")
    private String practitionersRemarks;
    
    @JsonProperty("WardTypeID")
    private Integer wardTypeID;
    
    @JsonProperty("RoomTypeID")
    private Integer roomTypeID;
    
    @JsonProperty("ChargesPerDay")
    private Double chargesPerDay;
    
    @JsonProperty("DateTransferred")
    private String dateTransferred;
    
    @JsonProperty("CreatedBy")
    private String createdBy;

    public String getAdmissionNo() {
        return admissionNo;
    }

    public void setAdmissionNo(String admissionNo) {
        this.admissionNo = admissionNo;
    }

    public String getPractitionerNo() {
        return practitionerNo;
    }

    public void setPractitionerNo(String practitionerNo) {
        this.practitionerNo = practitionerNo;
    }

    public String getPractitionersRemarks() {
        return practitionersRemarks;
    }

    public void setPractitionersRemarks(String practitionersRemarks) {
        this.practitionersRemarks = practitionersRemarks;
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

    public String getDateTransferred() {
        return dateTransferred;
    }

    public void setDateTransferred(String dateTransferred) {
        this.dateTransferred = dateTransferred;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}