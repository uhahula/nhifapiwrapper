package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PatientDischargeModel {
    @JsonProperty("AdmissionNo")
    private String admissionNo;
    
    @JsonProperty("PractitionerNo")
    private String practitionerNo;
    
    @JsonProperty("PractitionersRemarks")
    private String practitionersRemarks;
    
    @JsonProperty("DischargeTypeID")
    private Integer dischargeTypeID;
    
    @JsonProperty("DateDischarged")
    private String dateDischarged;
    
    @JsonProperty("DiagnosisAtDischarge")
    private String diagnosisAtDischarge;
    
    @JsonProperty("ReferredToFacilityCode")
    private String referredToFacilityCode;
    
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

    public Integer getDischargeTypeID() {
        return dischargeTypeID;
    }

    public void setDischargeTypeID(Integer dischargeTypeID) {
        this.dischargeTypeID = dischargeTypeID;
    }

    public String getDateDischarged() {
        return dateDischarged;
    }

    public void setDateDischarged(String dateDischarged) {
        this.dateDischarged = dateDischarged;
    }

    public String getDiagnosisAtDischarge() {
        return diagnosisAtDischarge;
    }

    public void setDiagnosisAtDischarge(String diagnosisAtDischarge) {
        this.diagnosisAtDischarge = diagnosisAtDischarge;
    }

    public String getReferredToFacilityCode() {
        return referredToFacilityCode;
    }

    public void setReferredToFacilityCode(String referredToFacilityCode) {
        this.referredToFacilityCode = referredToFacilityCode;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}