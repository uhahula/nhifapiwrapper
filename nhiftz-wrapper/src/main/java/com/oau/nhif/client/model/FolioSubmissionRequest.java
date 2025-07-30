package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolioSubmissionRequest {
    @JsonProperty("FacilityCode")
    private String facilityCode;
    
    @JsonProperty("ClaimYear")
    private Integer claimYear;
    
    @JsonProperty("ClaimMonth")
    private Integer claimMonth;
    
    @JsonProperty("FolioNo")
    private Integer folioNo;
    
    @JsonProperty("CardNo")
    private String cardNo;
    
    @JsonProperty("FirstName")
    private String firstName;
    
    @JsonProperty("LastName")
    private String lastName;
    
    @JsonProperty("Gender")
    private String gender;
    
    @JsonProperty("DateOfBirth")
    private String dateOfBirth;
    
    @JsonProperty("TelephoneNo")
    private String telephoneNo;
    
    @JsonProperty("PatientFileNo")
    private String patientFileNo;
    
    @JsonProperty("BillNo")
    private String billNo;
    
    @JsonProperty("ClinicalNotes")
    private String clinicalNotes;
    
    @JsonProperty("AuthorizationNo")
    private String authorizationNo;
    
    @JsonProperty("AttendanceDate")
    private String attendanceDate;
    
    @JsonProperty("PatientTypeCode")
    private String patientTypeCode;
    
    @JsonProperty("DateAdmitted")
    private String dateAdmitted;
    
    @JsonProperty("DateDischarged")
    private String dateDischarged;
    
    @JsonProperty("AttendingPractitioners")
    private List<String> attendingPractitioners;
    
    @JsonProperty("LateSubmissionReason")
    private String lateSubmissionReason;
    
    @JsonProperty("AmountClaimed")
    private Double amountClaimed;
    
    @JsonProperty("ConfirmationCode")
    private String confirmationCode;
    
    @JsonProperty("FolioDiseases")
    private List<FolioDisease> folioDiseases;
    
    @JsonProperty("FolioItems")
    private List<FolioItem> folioItems;
    
    @JsonProperty("Signatures")
    private List<FolioSignature> signatures;
    
    @JsonProperty("DateCreated")
    private String dateCreated;
    
    @JsonProperty("CreatedBy")
    private String createdBy;
    
    @JsonProperty("LastModified")
    private String lastModified;
    
    @JsonProperty("LastModifiedBy")
    private String lastModifiedBy;

    // Getters and setters
    public String getFacilityCode() {
        return facilityCode;
    }

    public void setFacilityCode(String facilityCode) {
        this.facilityCode = facilityCode;
    }

    public Integer getClaimYear() {
        return claimYear;
    }

    public void setClaimYear(Integer claimYear) {
        this.claimYear = claimYear;
    }

    public Integer getClaimMonth() {
        return claimMonth;
    }

    public void setClaimMonth(Integer claimMonth) {
        this.claimMonth = claimMonth;
    }

    public Integer getFolioNo() {
        return folioNo;
    }

    public void setFolioNo(Integer folioNo) {
        this.folioNo = folioNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getTelephoneNo() {
        return telephoneNo;
    }

    public void setTelephoneNo(String telephoneNo) {
        this.telephoneNo = telephoneNo;
    }

    public String getPatientFileNo() {
        return patientFileNo;
    }

    public void setPatientFileNo(String patientFileNo) {
        this.patientFileNo = patientFileNo;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getClinicalNotes() {
        return clinicalNotes;
    }

    public void setClinicalNotes(String clinicalNotes) {
        this.clinicalNotes = clinicalNotes;
    }

    public String getAuthorizationNo() {
        return authorizationNo;
    }

    public void setAuthorizationNo(String authorizationNo) {
        this.authorizationNo = authorizationNo;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getPatientTypeCode() {
        return patientTypeCode;
    }

    public void setPatientTypeCode(String patientTypeCode) {
        this.patientTypeCode = patientTypeCode;
    }

    public String getDateAdmitted() {
        return dateAdmitted;
    }

    public void setDateAdmitted(String dateAdmitted) {
        this.dateAdmitted = dateAdmitted;
    }

    public String getDateDischarged() {
        return dateDischarged;
    }

    public void setDateDischarged(String dateDischarged) {
        this.dateDischarged = dateDischarged;
    }

    public List<String> getAttendingPractitioners() {
        return attendingPractitioners;
    }

    public void setAttendingPractitioners(List<String> attendingPractitioners) {
        this.attendingPractitioners = attendingPractitioners;
    }

    public String getLateSubmissionReason() {
        return lateSubmissionReason;
    }

    public void setLateSubmissionReason(String lateSubmissionReason) {
        this.lateSubmissionReason = lateSubmissionReason;
    }

    public Double getAmountClaimed() {
        return amountClaimed;
    }

    public void setAmountClaimed(Double amountClaimed) {
        this.amountClaimed = amountClaimed;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public List<FolioDisease> getFolioDiseases() {
        return folioDiseases;
    }

    public void setFolioDiseases(List<FolioDisease> folioDiseases) {
        this.folioDiseases = folioDiseases;
    }

    public List<FolioItem> getFolioItems() {
        return folioItems;
    }

    public void setFolioItems(List<FolioItem> folioItems) {
        this.folioItems = folioItems;
    }

    public List<FolioSignature> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<FolioSignature> signatures) {
        this.signatures = signatures;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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
        return "FolioSubmissionRequest{" +
                "facilityCode='" + facilityCode + '\'' +
                ", claimYear=" + claimYear +
                ", claimMonth=" + claimMonth +
                ", folioNo=" + folioNo +
                ", cardNo='" + cardNo + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", telephoneNo='" + telephoneNo + '\'' +
                ", patientFileNo='" + patientFileNo + '\'' +
                ", billNo='" + billNo + '\'' +
                ", clinicalNotes='" + clinicalNotes + '\'' +
                ", authorizationNo='" + authorizationNo + '\'' +
                ", attendanceDate='" + attendanceDate + '\'' +
                ", patientTypeCode='" + patientTypeCode + '\'' +
                ", dateAdmitted='" + dateAdmitted + '\'' +
                ", dateDischarged='" + dateDischarged + '\'' +
                ", attendingPractitioners=" + attendingPractitioners +
                ", lateSubmissionReason='" + lateSubmissionReason + '\'' +
                ", amountClaimed=" + amountClaimed +
                ", confirmationCode='" + confirmationCode + '\'' +
                ", folioItems=" + (folioItems != null ? folioItems.size() + " items" : "null") +
                ", folioDiseases=" + (folioDiseases != null ? folioDiseases.size() + " diseases" : "null") +
                ", signatures=" + (signatures != null ? signatures.size() + " signatures" : "null") +
                ", dateCreated='" + dateCreated + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", lastModified='" + lastModified + '\'' +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                '}';
    }
}