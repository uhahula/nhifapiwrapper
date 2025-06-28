package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the percentage coverage for a specific service
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PercentCovered {
    @JsonProperty("authorizationNo")
    private String authorizationNo;
    
    @JsonProperty("itemCode")
    private String itemCode;
    
    @JsonProperty("itemDescription")
    private String itemDescription;
    
    @JsonProperty("percentCovered")
    private Double percentCovered;
    
    @JsonProperty("maxAmount")
    private Double maxAmount;
    
    @JsonProperty("copayAmount")
    private Double copayAmount;
    
    @JsonProperty("currency")
    private String currency;
    
    @JsonProperty("isValid")
    private Boolean isValid;
    
    @JsonProperty("message")
    private String message;

    public String getAuthorizationNo() {
        return authorizationNo;
    }

    public void setAuthorizationNo(String authorizationNo) {
        this.authorizationNo = authorizationNo;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Double getPercentCovered() {
        return percentCovered;
    }

    public void setPercentCovered(Double percentCovered) {
        this.percentCovered = percentCovered;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Double getCopayAmount() {
        return copayAmount;
    }

    public void setCopayAmount(Double copayAmount) {
        this.copayAmount = copayAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "PercentCovered{" +
                "authorizationNo='" + authorizationNo + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", percentCovered=" + percentCovered +
                ", maxAmount=" + maxAmount +
                ", copayAmount=" + copayAmount +
                ", currency='" + currency + '\'' +
                ", isValid=" + isValid +
                ", message='" + message + '\'' +
                '}';
    }
}