package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an eligibility check result for a specific service
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EligibilityCheck {
    @JsonProperty("CanAccess")
    private Boolean canAccess;
    
    @JsonProperty("Remarks")
    private String remarks;

    public Boolean getCanAccess() {
        return canAccess;
    }

    public void setCanAccess(Boolean canAccess) {
        this.canAccess = canAccess;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    // Legacy getters for backward compatibility
    public Boolean getIsEligible() {
        return canAccess;
    }

    public String getEligibilityMessage() {
        return remarks;
    }

    public Double getPercentCovered() {
        return null; // Not available in this response
    }

    public Boolean getRequiresApproval() {
        return null; // Not available in this response
    }

    @Override
    public String toString() {
        return "EligibilityCheck{" +
                "canAccess=" + canAccess +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}