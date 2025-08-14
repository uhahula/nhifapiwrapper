package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request model for practitioner attendance login
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PractitionerAttendanceRequest {
    
    @JsonProperty("nationalID")
    private String nationalID;
    
    @JsonProperty("practitionerNo")
    private String practitionerNo;
    
    @JsonProperty("biometricMethod")
    private String biometricMethod;
    
    @JsonProperty("fpCode")
    private String fpCode;
    
    @JsonProperty("imageData")
    private String imageData;
}