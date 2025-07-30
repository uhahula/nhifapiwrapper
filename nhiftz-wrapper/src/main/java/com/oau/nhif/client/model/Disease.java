package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Disease {
    @JsonProperty("DiseaseCode")
    private String diseaseCode;
    
    @JsonProperty("DiseaseName")
    private String diseaseName;
    
    @JsonProperty("IsNonSpecific")
    private Boolean isNonSpecific;
    
    @JsonProperty("ICDVersionCode")
    private String icdVersionCode;
    
    @JsonProperty("CreatedBy")
    private String createdBy;
    
    @JsonProperty("DateCreated")
    private String dateCreated;
    
    @JsonProperty("LastModifiedBy")
    private String lastModifiedBy;
    
    @JsonProperty("LastModified")
    private String lastModified;

    public String getDiseaseCode() {
        return diseaseCode;
    }

    public void setDiseaseCode(String diseaseCode) {
        this.diseaseCode = diseaseCode;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public Boolean getIsNonSpecific() {
        return isNonSpecific;
    }

    public void setIsNonSpecific(Boolean isNonSpecific) {
        this.isNonSpecific = isNonSpecific;
    }

    public String getIcdVersionCode() {
        return icdVersionCode;
    }

    public void setIcdVersionCode(String icdVersionCode) {
        this.icdVersionCode = icdVersionCode;
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
        return "Disease{" +
                "diseaseCode='" + diseaseCode + '\'' +
                ", diseaseName='" + diseaseName + '\'' +
                ", isNonSpecific=" + isNonSpecific +
                ", icdVersionCode='" + icdVersionCode + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                ", lastModified='" + lastModified + '\'' +
                '}';
    }
}