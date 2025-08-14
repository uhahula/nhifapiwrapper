package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorizationDetails {
    @JsonProperty("AuthorizationID")
    private String authorizationId;
    
    @JsonProperty("FacilityCode")
    private String facilityCode;
    
    @JsonProperty("CardNo")
    private String cardNo;
    
    @JsonProperty("AuthorizationStatus")
    private String authorizationStatus;
    
    @JsonProperty("RejectionReasonID")
    private Integer rejectionReasonId;
    
    @JsonProperty("SequenceNo")
    private Integer sequenceNo;
    
    @JsonProperty("AuthorizationNo")
    private String authorizationNo;
    
    @JsonProperty("IsProvisional")
    private Boolean isProvisional;
    
    @JsonProperty("AuthorizationDate")
    private String authorizationDate;
    
    @JsonProperty("CreatedBy")
    private String createdBy;
    
    @JsonProperty("DateCreated")
    private String dateCreated;
    
    @JsonProperty("LastModifiedBy")
    private String lastModifiedBy;
    
    @JsonProperty("LastModified")
    private String lastModified;
    
    @JsonProperty("SchemeID")
    private Integer schemeId;
    
    @JsonProperty("Remarks")
    private String remarks;
    
    @JsonProperty("VisitType")
    private String visitType;
    
    @JsonProperty("VisitTypeID")
    private Integer visitTypeId;
    
    @JsonProperty("CardExistence")
    private String cardExistence;
    
    @JsonProperty("CardStatusID")
    private Integer cardStatusId;
    
    @JsonProperty("CardStatus")
    private String cardStatus;
    
    @JsonProperty("DateOfBirth")
    private String dateOfBirth;
    
    @JsonProperty("Age")
    private Integer age;
    
    @JsonProperty("ExpiryDate")
    private String expiryDate;
    
    @JsonProperty("FirstName")
    private String firstName;
    
    @JsonProperty("MiddleName")
    private String middleName;
    
    @JsonProperty("LastName")
    private String lastName;
    
    @JsonProperty("FullName")
    private String fullName;
    
    @JsonProperty("Gender")
    private String gender;
    
    @JsonProperty("StatusDescription")
    private String statusDescription;
    
    @JsonProperty("IsActive")
    private Boolean isActive;
    
    @JsonProperty("IsValidCard")
    private Boolean isValidCard;
    
    @JsonProperty("MembershipNo")
    private String membershipNo;
    
    @JsonProperty("EmployerNo")
    private String employerNo;
    
    @JsonProperty("AuthorizationDateSerial")
    private Long authorizationDateSerial;
    
    @JsonProperty("MemberCategoryID")
    private Integer memberCategoryId;
    
    @JsonProperty("MethodUsed")
    private String methodUsed;
    
    @JsonProperty("ProductCode")
    private String productCode;
    
    @JsonProperty("HasActivePolicy")
    private Boolean hasActivePolicy;
    
    @JsonProperty("TokenNo")
    private String tokenNo;
    
    @JsonProperty("ClientID")
    private String clientId;
    
    @JsonProperty("IsSynced")
    private Boolean isSynced;
    
    @JsonProperty("LatestContributionMonth")
    private Long latestContributionMonth;
    
    @JsonProperty("LatestMonthAllowed")
    private Long latestMonthAllowed;
    
    @JsonProperty("ServiceYear")
    private Integer serviceYear;
    
    @JsonProperty("WCFAuthorizationNo")
    private String wcfAuthorizationNo;
    
    @JsonProperty("WCFNotificationNo")
    private String wcfNotificationNo;
    
    @JsonProperty("PolicyID")
    private String policyId;
    
    @JsonProperty("BiometricMethod")
    private String biometricMethod;
    
    @JsonProperty("AgreementId")
    private String agreementId;

    // Getters and setters
    public String getAuthorizationId() { return authorizationId; }
    public void setAuthorizationId(String authorizationId) { this.authorizationId = authorizationId; }

    public String getFacilityCode() { return facilityCode; }
    public void setFacilityCode(String facilityCode) { this.facilityCode = facilityCode; }

    public String getCardNo() { return cardNo; }
    public void setCardNo(String cardNo) { this.cardNo = cardNo; }

    public String getAuthorizationStatus() { return authorizationStatus; }
    public void setAuthorizationStatus(String authorizationStatus) { this.authorizationStatus = authorizationStatus; }

    public Integer getRejectionReasonId() { return rejectionReasonId; }
    public void setRejectionReasonId(Integer rejectionReasonId) { this.rejectionReasonId = rejectionReasonId; }

    public Integer getSequenceNo() { return sequenceNo; }
    public void setSequenceNo(Integer sequenceNo) { this.sequenceNo = sequenceNo; }

    public String getAuthorizationNo() { return authorizationNo; }
    public void setAuthorizationNo(String authorizationNo) { this.authorizationNo = authorizationNo; }

    public Boolean getIsProvisional() { return isProvisional; }
    public void setIsProvisional(Boolean isProvisional) { this.isProvisional = isProvisional; }

    public String getAuthorizationDate() { return authorizationDate; }
    public void setAuthorizationDate(String authorizationDate) { this.authorizationDate = authorizationDate; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getDateCreated() { return dateCreated; }
    public void setDateCreated(String dateCreated) { this.dateCreated = dateCreated; }

    public String getLastModifiedBy() { return lastModifiedBy; }
    public void setLastModifiedBy(String lastModifiedBy) { this.lastModifiedBy = lastModifiedBy; }

    public String getLastModified() { return lastModified; }
    public void setLastModified(String lastModified) { this.lastModified = lastModified; }

    public Integer getSchemeId() { return schemeId; }
    public void setSchemeId(Integer schemeId) { this.schemeId = schemeId; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getVisitType() { return visitType; }
    public void setVisitType(String visitType) { this.visitType = visitType; }

    public Integer getVisitTypeId() { return visitTypeId; }
    public void setVisitTypeId(Integer visitTypeId) { this.visitTypeId = visitTypeId; }

    public String getCardExistence() { return cardExistence; }
    public void setCardExistence(String cardExistence) { this.cardExistence = cardExistence; }

    public Integer getCardStatusId() { return cardStatusId; }
    public void setCardStatusId(Integer cardStatusId) { this.cardStatusId = cardStatusId; }

    public String getCardStatus() { return cardStatus; }
    public void setCardStatus(String cardStatus) { this.cardStatus = cardStatus; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getStatusDescription() { return statusDescription; }
    public void setStatusDescription(String statusDescription) { this.statusDescription = statusDescription; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Boolean getIsValidCard() { return isValidCard; }
    public void setIsValidCard(Boolean isValidCard) { this.isValidCard = isValidCard; }

    public String getMembershipNo() { return membershipNo; }
    public void setMembershipNo(String membershipNo) { this.membershipNo = membershipNo; }

    public String getEmployerNo() { return employerNo; }
    public void setEmployerNo(String employerNo) { this.employerNo = employerNo; }

    public Long getAuthorizationDateSerial() { return authorizationDateSerial; }
    public void setAuthorizationDateSerial(Long authorizationDateSerial) { this.authorizationDateSerial = authorizationDateSerial; }

    public Integer getMemberCategoryId() { return memberCategoryId; }
    public void setMemberCategoryId(Integer memberCategoryId) { this.memberCategoryId = memberCategoryId; }

    public String getMethodUsed() { return methodUsed; }
    public void setMethodUsed(String methodUsed) { this.methodUsed = methodUsed; }

    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }

    public Boolean getHasActivePolicy() { return hasActivePolicy; }
    public void setHasActivePolicy(Boolean hasActivePolicy) { this.hasActivePolicy = hasActivePolicy; }

    public String getTokenNo() { return tokenNo; }
    public void setTokenNo(String tokenNo) { this.tokenNo = tokenNo; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public Boolean getIsSynced() { return isSynced; }
    public void setIsSynced(Boolean isSynced) { this.isSynced = isSynced; }

    public Long getLatestContributionMonth() { return latestContributionMonth; }
    public void setLatestContributionMonth(Long latestContributionMonth) { this.latestContributionMonth = latestContributionMonth; }

    public Long getLatestMonthAllowed() { return latestMonthAllowed; }
    public void setLatestMonthAllowed(Long latestMonthAllowed) { this.latestMonthAllowed = latestMonthAllowed; }

    public Integer getServiceYear() { return serviceYear; }
    public void setServiceYear(Integer serviceYear) { this.serviceYear = serviceYear; }

    public String getWcfAuthorizationNo() { return wcfAuthorizationNo; }
    public void setWcfAuthorizationNo(String wcfAuthorizationNo) { this.wcfAuthorizationNo = wcfAuthorizationNo; }

    public String getWcfNotificationNo() { return wcfNotificationNo; }
    public void setWcfNotificationNo(String wcfNotificationNo) { this.wcfNotificationNo = wcfNotificationNo; }

    public String getPolicyId() { return policyId; }
    public void setPolicyId(String policyId) { this.policyId = policyId; }

    public String getBiometricMethod() { return biometricMethod; }
    public void setBiometricMethod(String biometricMethod) { this.biometricMethod = biometricMethod; }

    public String getAgreementId() { return agreementId; }
    public void setAgreementId(String agreementId) { this.agreementId = agreementId; }
}