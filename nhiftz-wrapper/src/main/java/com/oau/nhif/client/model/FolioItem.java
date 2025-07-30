package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolioItem {
    @JsonProperty("ItemCode")
    private String itemCode;
    
    @JsonProperty("ItemName")
    private String itemName;
    
    @JsonProperty("ItemTypeID")
    private Integer itemTypeID;
    
    @JsonProperty("UnitPrice")
    private Double unitPrice;
    
    @JsonProperty("ItemQuantity")
    private Integer itemQuantity;
    
    @JsonProperty("AmountClaimed")
    private Double amountClaimed;
    
    @JsonProperty("OtherDetails")
    private String otherDetails;
    
    @JsonProperty("ApprovalRefNo")
    private String approvalRefNo;
    
    @JsonProperty("CreatedBy")
    private String createdBy;
    
    @JsonProperty("DateCreated")
    private String dateCreated;
    
    @JsonProperty("LastModifiedBy")
    private String lastModifiedBy;
    
    @JsonProperty("LastModified")
    private String lastModified;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getItemTypeID() {
        return itemTypeID;
    }

    public void setItemTypeID(Integer itemTypeID) {
        this.itemTypeID = itemTypeID;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public Double getAmountClaimed() {
        return amountClaimed;
    }

    public void setAmountClaimed(Double amountClaimed) {
        this.amountClaimed = amountClaimed;
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        this.otherDetails = otherDetails;
    }

    public String getApprovalRefNo() {
        return approvalRefNo;
    }

    public void setApprovalRefNo(String approvalRefNo) {
        this.approvalRefNo = approvalRefNo;
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
        return "FolioItem{" +
                "itemCode='" + itemCode + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemTypeID=" + itemTypeID +
                ", unitPrice=" + unitPrice +
                ", itemQuantity=" + itemQuantity +
                ", amountClaimed=" + amountClaimed +
                ", otherDetails='" + otherDetails + '\'' +
                ", approvalRefNo='" + approvalRefNo + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                ", lastModified='" + lastModified + '\'' +
                '}';
    }
}