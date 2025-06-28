package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Healthcare Facility in the NHIF system
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Facility {
    @JsonProperty("FacilityCode")
    private String facilityCode;
    
    @JsonProperty("FacilityName")
    private String facilityName;
    
    @JsonProperty("RegistrationNo")
    private String registrationNo;
    
    @JsonProperty("FacilityLevelCode")
    private String facilityLevelCode;

    public String getFacilityCode() {
        return facilityCode;
    }

    public void setFacilityCode(String facilityCode) {
        this.facilityCode = facilityCode;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getFacilityLevelCode() {
        return facilityLevelCode;
    }

    public void setFacilityLevelCode(String facilityLevelCode) {
        this.facilityLevelCode = facilityLevelCode;
    }

    @Override
    public String toString() {
        return "Facility{" +
                "facilityCode='" + facilityCode + '\'' +
                ", facilityName='" + facilityName + '\'' +
                ", registrationNo='" + registrationNo + '\'' +
                ", facilityLevelCode='" + facilityLevelCode + '\'' +
                '}';
    }
}