package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdmissionType {
    @JsonProperty("AdmissionTypeID")
    private Integer admissionTypeID;
    
    @JsonProperty("AdmissionTypeName")
    private String admissionTypeName;
    
    @JsonProperty("Alias")
    private String alias;
    
    @JsonProperty("CreatedBy")
    private String createdBy;
    
    @JsonProperty("DateCreated")
    private String dateCreated;
    
    @JsonProperty("LastModifiedBy")
    private String lastModifiedBy;
    
    @JsonProperty("LastModified")
    private String lastModified;

    public Integer getAdmissionTypeID() {
        return admissionTypeID;
    }

    public void setAdmissionTypeID(Integer admissionTypeID) {
        this.admissionTypeID = admissionTypeID;
    }

    public String getAdmissionTypeName() {
        return admissionTypeName;
    }

    public void setAdmissionTypeName(String admissionTypeName) {
        this.admissionTypeName = admissionTypeName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }
}