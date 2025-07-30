package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DischargeType {
    @JsonProperty("DischargeTypeID")
    private Integer dischargeTypeID;
    
    @JsonProperty("DischargeTypeName")
    private String dischargeTypeName;
    
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

    public Integer getDischargeTypeID() {
        return dischargeTypeID;
    }

    public void setDischargeTypeID(Integer dischargeTypeID) {
        this.dischargeTypeID = dischargeTypeID;
    }

    public String getDischargeTypeName() {
        return dischargeTypeName;
    }

    public void setDischargeTypeName(String dischargeTypeName) {
        this.dischargeTypeName = dischargeTypeName;
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