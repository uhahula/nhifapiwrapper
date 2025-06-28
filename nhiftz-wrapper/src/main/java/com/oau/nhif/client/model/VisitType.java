package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Visit Type in the NHIF system
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VisitType {
    @JsonProperty("VisitTypeID")
    private Integer visitTypeID;
    
    @JsonProperty("VisitTypeName")
    private String visitTypeName;
    
    @JsonProperty("RequiredInput")
    private String requiredInput;
    
    @JsonProperty("Alias")
    private String alias;
    
    @JsonProperty("RequiresRemarks")
    private Boolean requiresRemarks;
    
    @JsonProperty("RequiresReferralNo")
    private Boolean requiresReferralNo;
    
    @JsonProperty("Description")
    private String description;
    
    @JsonProperty("MaximumVisitPerMonth")
    private Integer maximumVisitPerMonth;
    
    @JsonProperty("CreatedBy")
    private String createdBy;

    public Integer getVisitTypeID() {
        return visitTypeID;
    }

    public void setVisitTypeID(Integer visitTypeID) {
        this.visitTypeID = visitTypeID;
    }

    public String getVisitTypeName() {
        return visitTypeName;
    }

    public void setVisitTypeName(String visitTypeName) {
        this.visitTypeName = visitTypeName;
    }

    public String getRequiredInput() {
        return requiredInput;
    }

    public void setRequiredInput(String requiredInput) {
        this.requiredInput = requiredInput;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Boolean getRequiresRemarks() {
        return requiresRemarks;
    }

    public void setRequiresRemarks(Boolean requiresRemarks) {
        this.requiresRemarks = requiresRemarks;
    }

    public Boolean getRequiresReferralNo() {
        return requiresReferralNo;
    }

    public void setRequiresReferralNo(Boolean requiresReferralNo) {
        this.requiresReferralNo = requiresReferralNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaximumVisitPerMonth() {
        return maximumVisitPerMonth;
    }

    public void setMaximumVisitPerMonth(Integer maximumVisitPerMonth) {
        this.maximumVisitPerMonth = maximumVisitPerMonth;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "VisitType{" +
                "visitTypeID=" + visitTypeID +
                ", visitTypeName='" + visitTypeName + '\'' +
                ", requiredInput='" + requiredInput + '\'' +
                ", alias='" + alias + '\'' +
                ", requiresRemarks=" + requiresRemarks +
                ", requiresReferralNo=" + requiresReferralNo +
                ", description='" + description + '\'' +
                ", maximumVisitPerMonth=" + maximumVisitPerMonth +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}