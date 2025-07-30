package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PricePackage {
    @JsonProperty("PackageID")
    private Integer packageID;
    
    @JsonProperty("PackageName")
    private String packageName;
    
    @JsonProperty("PricingSchemeID")
    private Integer pricingSchemeID;
    
    @JsonProperty("FromMonthSerial")
    private Integer fromMonthSerial;
    
    @JsonProperty("ToMonthSerial")
    private Integer toMonthSerial;
    
    @JsonProperty("Alias")
    private String alias;
    
    @JsonProperty("IsActive")
    private Boolean isActive;
    
    @JsonProperty("CreatedBy")
    private String createdBy;
    
    @JsonProperty("DateCreated")
    private String dateCreated;
    
    @JsonProperty("LastModifiedBy")
    private String lastModifiedBy;
    
    @JsonProperty("LastModified")
    private String lastModified;

    public Integer getPackageID() {
        return packageID;
    }

    public void setPackageID(Integer packageID) {
        this.packageID = packageID;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Integer getPricingSchemeID() {
        return pricingSchemeID;
    }

    public void setPricingSchemeID(Integer pricingSchemeID) {
        this.pricingSchemeID = pricingSchemeID;
    }

    public Integer getFromMonthSerial() {
        return fromMonthSerial;
    }

    public void setFromMonthSerial(Integer fromMonthSerial) {
        this.fromMonthSerial = fromMonthSerial;
    }

    public Integer getToMonthSerial() {
        return toMonthSerial;
    }

    public void setToMonthSerial(Integer toMonthSerial) {
        this.toMonthSerial = toMonthSerial;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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
        return "PricePackage{" +
                "packageID=" + packageID +
                ", packageName='" + packageName + '\'' +
                ", pricingSchemeID=" + pricingSchemeID +
                ", fromMonthSerial=" + fromMonthSerial +
                ", toMonthSerial=" + toMonthSerial +
                ", alias='" + alias + '\'' +
                ", isActive=" + isActive +
                ", createdBy='" + createdBy + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                ", lastModified='" + lastModified + '\'' +
                '}';
    }
}