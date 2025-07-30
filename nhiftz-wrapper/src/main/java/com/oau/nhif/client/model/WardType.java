package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WardType {
    @JsonProperty("WardTypeID")
    private Integer wardTypeID;
    
    @JsonProperty("WardTypeName")
    private String wardTypeName;
    
    @JsonProperty("NotificationRequiredAfter")
    private Integer notificationRequiredAfter;
    
    @JsonProperty("ItemCode")
    private String itemCode;
    
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

    public Integer getWardTypeID() {
        return wardTypeID;
    }

    public void setWardTypeID(Integer wardTypeID) {
        this.wardTypeID = wardTypeID;
    }

    public String getWardTypeName() {
        return wardTypeName;
    }

    public void setWardTypeName(String wardTypeName) {
        this.wardTypeName = wardTypeName;
    }

    public Integer getNotificationRequiredAfter() {
        return notificationRequiredAfter;
    }

    public void setNotificationRequiredAfter(Integer notificationRequiredAfter) {
        this.notificationRequiredAfter = notificationRequiredAfter;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
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