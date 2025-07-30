package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemType {
    @JsonProperty("ItemTypeID")
    private Integer itemTypeID;
    
    @JsonProperty("TypeName")
    private String typeName;
    
    @JsonProperty("Alias")
    private String alias;
    
    @JsonProperty("ItemGroup")
    private String itemGroup;
    
    @JsonProperty("DisplayItem")
    private Boolean displayItem;
    
    @JsonProperty("CreatedBy")
    private String createdBy;
    
    @JsonProperty("DateCreated")
    private String dateCreated;
    
    @JsonProperty("LastModifiedBy")
    private String lastModifiedBy;
    
    @JsonProperty("LastModified")
    private String lastModified;

    public Integer getItemTypeID() {
        return itemTypeID;
    }

    public void setItemTypeID(Integer itemTypeID) {
        this.itemTypeID = itemTypeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public Boolean getDisplayItem() {
        return displayItem;
    }

    public void setDisplayItem(Boolean displayItem) {
        this.displayItem = displayItem;
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
        return "ItemType{" +
                "itemTypeID=" + itemTypeID +
                ", typeName='" + typeName + '\'' +
                ", alias='" + alias + '\'' +
                ", itemGroup='" + itemGroup + '\'' +
                ", displayItem=" + displayItem +
                ", createdBy='" + createdBy + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                ", lastModified='" + lastModified + '\'' +
                '}';
    }
}