package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entry returned by {@code /api/Verification/GetCardVerifiers}.
 * Used to populate the verifier dropdown on authorize forms.
 *
 * NHIF has not published an exhaustive schema for this endpoint, so any
 * field whose JSON name does not match below will be ignored rather than
 * blowing up deserialization.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardVerifier {
    @JsonProperty("VerifierID")
    private Integer verifierID;

    @JsonProperty("VerifierName")
    private String verifierName;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("IsActive")
    private Boolean isActive;

    public Integer getVerifierID() { return verifierID; }
    public void setVerifierID(Integer verifierID) { this.verifierID = verifierID; }

    public String getVerifierName() { return verifierName; }
    public void setVerifierName(String verifierName) { this.verifierName = verifierName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    @Override
    public String toString() {
        return "CardVerifier{verifierID=" + verifierID
            + ", verifierName='" + verifierName + "'}";
    }
}
