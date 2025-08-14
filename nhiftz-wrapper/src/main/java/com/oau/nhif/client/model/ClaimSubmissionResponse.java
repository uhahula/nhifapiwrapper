package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response model for folio submission
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClaimSubmissionResponse {
    // Success response fields (HTTP 200/100)
    @JsonProperty("SubmissionID")
    private String submissionId;
    
    @JsonProperty("AcknowledgementNo")
    private String acknowledgementNo;
    
    @JsonProperty("DateSubmitted")
    private String dateSubmitted;
    
    // Error response fields (HTTP 500)
    @JsonProperty("StatusCode")
    private Integer statusCode;
    
    @JsonProperty("Message")
    private String message;
    
    @JsonProperty("ReasonPhrase")
    private String reasonPhrase;
    
    // Getters and setters
    public String getSubmissionId() { return submissionId; }
    public void setSubmissionId(String submissionId) { this.submissionId = submissionId; }
    
    public String getAcknowledgementNo() { return acknowledgementNo; }
    public void setAcknowledgementNo(String acknowledgementNo) { this.acknowledgementNo = acknowledgementNo; }
    
    public String getDateSubmitted() { return dateSubmitted; }
    public void setDateSubmitted(String dateSubmitted) { this.dateSubmitted = dateSubmitted; }
    
    public Integer getStatusCode() { return statusCode; }
    public void setStatusCode(Integer statusCode) { this.statusCode = statusCode; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getReasonPhrase() { return reasonPhrase; }
    public void setReasonPhrase(String reasonPhrase) { this.reasonPhrase = reasonPhrase; }
    
    public boolean isSuccess() {
        return statusCode == null || (statusCode >= 100 && statusCode < 300);
    }
    
    @Override
    public String toString() {
        if (isSuccess()) {
            return "ClaimSubmissionResponse{" +
                    "submissionId='" + submissionId + '\'' +
                    ", acknowledgementNo='" + acknowledgementNo + '\'' +
                    ", dateSubmitted='" + dateSubmitted + '\'' +
                    '}';
        } else {
            return "ClaimSubmissionResponse{" +
                    "statusCode=" + statusCode +
                    ", message='" + message + '\'' +
                    ", reasonPhrase='" + reasonPhrase + '\'' +
                    '}';
        }
    }
}
