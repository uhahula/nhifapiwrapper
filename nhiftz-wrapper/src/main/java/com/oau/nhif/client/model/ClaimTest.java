package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response model for test claim endpoint
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClaimTest {
    @JsonProperty("Status")
    private String status;

    public ClaimTest() {}

    public ClaimTest(String status) {
        this.status = status;
    }
    
    // Getters and setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
