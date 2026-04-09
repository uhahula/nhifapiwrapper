package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request model for generating a Point-of-Care reference number
 * via biometric verification (POST /api/Verification/GeneratePOCReferenceNo).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointOfCareReferenceRequest {

    @JsonProperty("pointOfCareID")
    private int pointOfCareID;

    @JsonProperty("authorizationNo")
    private String authorizationNo;

    @JsonProperty("practitionerNo")
    private String practitionerNo;

    @JsonProperty("biometricMethod")
    private String biometricMethod;

    @JsonProperty("fpCode")
    private String fpCode;

    @JsonProperty("imageData")
    private String imageData;
}
