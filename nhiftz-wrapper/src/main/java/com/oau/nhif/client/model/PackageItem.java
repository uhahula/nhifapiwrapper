package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PackageItem {
    @JsonProperty("ItemCode")
    private String itemCode;
    
    @JsonProperty("ItemTypeID")
    private Integer itemTypeID;
    
    @JsonProperty("ItemName")
    private String itemName;
    
    @JsonProperty("SubGroup")
    private String subGroup;
    
    @JsonProperty("Strength")
    private String strength;
    
    @JsonProperty("Dosage")
    private String dosage;
    
    @JsonProperty("CalculatedPerDay")
    private Boolean calculatedPerDay;
    
    @JsonProperty("ServiceTypeID")
    private Integer serviceTypeID;
    
    @JsonProperty("IsRestricted")
    private Boolean isRestricted;
    
    @JsonProperty("ServiceInterval")
    private String serviceInterval;
    
    @JsonProperty("TypeOfInterval")
    private String typeOfInterval;
    
    @JsonProperty("WaitingPeriod")
    private Integer waitingPeriod;
    
    @JsonProperty("TypeOfPeriod")
    private String typeOfPeriod;
    
    @JsonProperty("Eligibility")
    private String eligibility;
    
    @JsonProperty("CommonPrice")
    private Double commonPrice;
    
    @JsonProperty("PercentCovered")
    private Integer percentCovered;
    
    @JsonProperty("AvailableInLevels")
    private String availableInLevels;
    
    @JsonProperty("PractitionerQualifications")
    private String practitionerQualifications;
    
    @JsonProperty("IsActive")
    private Boolean isActive;
    
    @JsonProperty("CreatedBy")
    private String createdBy;
    
    @JsonProperty("DateCreated")
    private String dateCreated;
    
    @JsonProperty("LastModified")
    private String lastModified;
    
    @JsonProperty("LastModifiedBy")
    private String lastModifiedBy;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
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

    public String getSubGroup() {
        return subGroup;
    }

    public void setSubGroup(String subGroup) {
        this.subGroup = subGroup;
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

    public Boolean getCalculatedPerDay() {
        return calculatedPerDay;
    }

    public void setCalculatedPerDay(Boolean calculatedPerDay) {
        this.calculatedPerDay = calculatedPerDay;
    }

    public Integer getServiceTypeID() {
        return serviceTypeID;
    }

    public void setServiceTypeID(Integer serviceTypeID) {
        this.serviceTypeID = serviceTypeID;
    }

    public Boolean getIsRestricted() {
        return isRestricted;
    }

    public void setIsRestricted(Boolean isRestricted) {
        this.isRestricted = isRestricted;
    }

    public String getServiceInterval() {
        return serviceInterval;
    }

    public void setServiceInterval(String serviceInterval) {
        this.serviceInterval = serviceInterval;
    }

    public String getTypeOfInterval() {
        return typeOfInterval;
    }

    public void setTypeOfInterval(String typeOfInterval) {
        this.typeOfInterval = typeOfInterval;
    }

    public Integer getWaitingPeriod() {
        return waitingPeriod;
    }

    public void setWaitingPeriod(Integer waitingPeriod) {
        this.waitingPeriod = waitingPeriod;
    }

    public String getTypeOfPeriod() {
        return typeOfPeriod;
    }

    public void setTypeOfPeriod(String typeOfPeriod) {
        this.typeOfPeriod = typeOfPeriod;
    }

    public String getEligibility() {
        return eligibility;
    }

    public void setEligibility(String eligibility) {
        this.eligibility = eligibility;
    }

    public Double getCommonPrice() {
        return commonPrice;
    }

    public void setCommonPrice(Double commonPrice) {
        this.commonPrice = commonPrice;
    }

    public Integer getPercentCovered() {
        return percentCovered;
    }

    public void setPercentCovered(Integer percentCovered) {
        this.percentCovered = percentCovered;
    }

    public String getAvailableInLevels() {
        return availableInLevels;
    }

    public void setAvailableInLevels(String availableInLevels) {
        this.availableInLevels = availableInLevels;
    }

    public String getPractitionerQualifications() {
        return practitionerQualifications;
    }

    public void setPractitionerQualifications(String practitionerQualifications) {
        this.practitionerQualifications = practitionerQualifications;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public String toString() {
        return "PackageItem{" +
                "itemCode='" + itemCode + '\'' +
                ", itemTypeID=" + itemTypeID +
                ", itemName='" + itemName + '\'' +
                ", subGroup='" + subGroup + '\'' +
                ", strength='" + strength + '\'' +
                ", dosage='" + dosage + '\'' +
                ", calculatedPerDay=" + calculatedPerDay +
                ", serviceTypeID=" + serviceTypeID +
                ", isRestricted=" + isRestricted +
                ", serviceInterval='" + serviceInterval + '\'' +
                ", typeOfInterval='" + typeOfInterval + '\'' +
                ", waitingPeriod=" + waitingPeriod +
                ", typeOfPeriod='" + typeOfPeriod + '\'' +
                ", eligibility='" + eligibility + '\'' +
                ", commonPrice=" + commonPrice +
                ", percentCovered=" + percentCovered +
                ", availableInLevels='" + availableInLevels + '\'' +
                ", practitionerQualifications='" + practitionerQualifications + '\'' +
                ", isActive=" + isActive +
                ", createdBy='" + createdBy + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", lastModified='" + lastModified + '\'' +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                '}';
    }
}