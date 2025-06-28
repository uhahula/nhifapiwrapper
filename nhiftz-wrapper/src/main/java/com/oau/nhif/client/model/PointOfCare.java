package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Point of Care in the NHIF system
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PointOfCare {
    @JsonProperty("PointOfCareID")
    private Integer pointOfCareID;
    
    @JsonProperty("PointOfCareName")
    private String pointOfCareName;
    
    @JsonProperty("PointOfCareCode")
    private String pointOfCareCode;
    
    @JsonProperty("CreatedBy")
    private String createdBy;
    
    @JsonProperty("DateCreated")
    private String dateCreated;
    
    @JsonProperty("LastModifiedBy")
    private String lastModifiedBy;
    
    @JsonProperty("LastModified")
    private String lastModified;

    public Integer getPointOfCareID() {
        return pointOfCareID;
    }

    public void setPointOfCareID(Integer pointOfCareID) {
        this.pointOfCareID = pointOfCareID;
    }

    public String getPointOfCareName() {
        return pointOfCareName;
    }

    public void setPointOfCareName(String pointOfCareName) {
        this.pointOfCareName = pointOfCareName;
    }

    public String getPointOfCareCode() {
        return pointOfCareCode;
    }

    public void setPointOfCareCode(String pointOfCareCode) {
        this.pointOfCareCode = pointOfCareCode;
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

    @Override
    public String toString() {
        return "PointOfCare{" +
                "pointOfCareID=" + pointOfCareID +
                ", pointOfCareName='" + pointOfCareName + '\'' +
                ", pointOfCareCode='" + pointOfCareCode + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                ", lastModified='" + lastModified + '\'' +
                '}';
    }
}