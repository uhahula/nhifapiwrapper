package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a patient's visit history
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PatientVisit {
    @JsonProperty("CardNo")
    private String cardNo;
    
    @JsonProperty("FacilityName")
    private String facilityName;
    
    @JsonProperty("AuthorizationStatus")
    private String authorizationStatus;
    
    @JsonProperty("AuthorizationNo")
    private String authorizationNo;
    
    @JsonProperty("AuthorizationDate")
    private String authorizationDate;
    
    @JsonProperty("CreatedBy")
    private String createdBy;
    
    // Legacy fields for backward compatibility
    @JsonProperty("visitDate")
    private String visitDate;
    
    @JsonProperty("facilityCode")
    private String facilityCode;
    
    @JsonProperty("status")
    private String status;
    
    // Getters and setters
    public String getCardNo() { return cardNo; }
    public void setCardNo(String cardNo) { this.cardNo = cardNo; }
    
    public String getFacilityName() { return facilityName; }
    public void setFacilityName(String facilityName) { this.facilityName = facilityName; }
    
    public String getAuthorizationStatus() { return authorizationStatus; }
    public void setAuthorizationStatus(String authorizationStatus) { this.authorizationStatus = authorizationStatus; }
    
    public String getAuthorizationNo() { return authorizationNo; }
    public void setAuthorizationNo(String authorizationNo) { this.authorizationNo = authorizationNo; }
    
    public String getAuthorizationDate() { return authorizationDate; }
    public void setAuthorizationDate(String authorizationDate) { this.authorizationDate = authorizationDate; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    // Legacy getters for backward compatibility
    public String getVisitDate() { 
        return authorizationDate != null ? authorizationDate : visitDate; 
    }
    public void setVisitDate(String visitDate) { this.visitDate = visitDate; }
    
    public String getFacilityCode() { return facilityCode; }
    public void setFacilityCode(String facilityCode) { this.facilityCode = facilityCode; }
    
    public String getStatus() { 
        return authorizationStatus != null ? authorizationStatus : status; 
    }
    public void setStatus(String status) { this.status = status; }
    
    @Override
    public String toString() {
        return "PatientVisit{" +
                "cardNo='" + cardNo + '\'' +
                ", facilityName='" + facilityName + '\'' +
                ", authorizationStatus='" + authorizationStatus + '\'' +
                ", authorizationDate='" + authorizationDate + '\'' +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}
