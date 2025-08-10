package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FacilityPackageItem {
    @JsonProperty("ItemCode")
    private String itemCode;
    
    @JsonProperty("PriceCode")
    private String priceCode;
    
    @JsonProperty("ItemTypeID")
    private Integer itemTypeID;
    
    @JsonProperty("ItemName")
    private String itemName;
    
    @JsonProperty("Strength")
    private String strength;
    
    @JsonProperty("Dosage")
    private String dosage;
    
    @JsonProperty("PackageID")
    private Integer packageID;
    
    @JsonProperty("SchemeID")
    private Integer schemeID;
    
    @JsonProperty("UnitPrice")
    private Double unitPrice;
    
    @JsonProperty("IsRestricted")
    private Boolean isRestricted;
    
    @JsonProperty("MaximumQuantity")
    private Integer maximumQuantity;
    
    @JsonProperty("MaximumQuantityOutPatient")
    private Integer maximumQuantityOutPatient;
    
    @JsonProperty("MaximumQuantityInPatient")
    private Integer maximumQuantityInPatient;
    
    @JsonProperty("HasCoPayment")
    private Boolean hasCoPayment;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getPriceCode() {
        return priceCode;
    }

    public void setPriceCode(String priceCode) {
        this.priceCode = priceCode;
    }

    public Integer getItemTypeID() {
        return itemTypeID;
    }

    public void setItemTypeID(Integer itemTypeID) {
        this.itemTypeID = itemTypeID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public Integer getPackageID() {
        return packageID;
    }

    public void setPackageID(Integer packageID) {
        this.packageID = packageID;
    }

    public Integer getSchemeID() {
        return schemeID;
    }

    public void setSchemeID(Integer schemeID) {
        this.schemeID = schemeID;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Boolean getIsRestricted() {
        return isRestricted;
    }

    public void setIsRestricted(Boolean isRestricted) {
        this.isRestricted = isRestricted;
    }

    public Integer getMaximumQuantity() {
        return maximumQuantity;
    }

    public void setMaximumQuantity(Integer maximumQuantity) {
        this.maximumQuantity = maximumQuantity;
    }

    public Integer getMaximumQuantityOutPatient() {
        return maximumQuantityOutPatient;
    }

    public void setMaximumQuantityOutPatient(Integer maximumQuantityOutPatient) {
        this.maximumQuantityOutPatient = maximumQuantityOutPatient;
    }

    public Integer getMaximumQuantityInPatient() {
        return maximumQuantityInPatient;
    }

    public void setMaximumQuantityInPatient(Integer maximumQuantityInPatient) {
        this.maximumQuantityInPatient = maximumQuantityInPatient;
    }

    public Boolean getHasCoPayment() {
        return hasCoPayment;
    }

    public void setHasCoPayment(Boolean hasCoPayment) {
        this.hasCoPayment = hasCoPayment;
    }

    @Override
    public String toString() {
        return "FacilityPackageItem{" +
                "itemCode='" + itemCode + '\'' +
                ", priceCode='" + priceCode + '\'' +
                ", itemTypeID=" + itemTypeID +
                ", itemName='" + itemName + '\'' +
                ", strength='" + strength + '\'' +
                ", dosage='" + dosage + '\'' +
                ", packageID=" + packageID +
                ", schemeID=" + schemeID +
                ", unitPrice=" + unitPrice +
                ", isRestricted=" + isRestricted +
                ", maximumQuantity=" + maximumQuantity +
                ", maximumQuantityOutPatient=" + maximumQuantityOutPatient +
                ", maximumQuantityInPatient=" + maximumQuantityInPatient +
                ", hasCoPayment=" + hasCoPayment +
                '}';
    }
}