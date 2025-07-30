package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response model for card authorization
 */
public class CardAuthorizationResponse {
    @JsonProperty("AuthorizationID")
    private String authorizationID;
    
    @JsonProperty("CardNo")
    private String cardNo;
    
    @JsonProperty("MembershipNo")
    private String membershipNo;
    
    @JsonProperty("EmployerNo")
    private String employerNo;
    
    @JsonProperty("EmployerName")
    private String employerName;
    
    @JsonProperty("HasSupplementary")
    private String hasSupplementary;
    
    @JsonProperty("SupplementaryAgreementId")
    private String supplementaryAgreementId;
    
    @JsonProperty("SchemeID")
    private Integer schemeID;
    
    @JsonProperty("SchemeName")
    private String schemeName;
    
    @JsonProperty("CardExistence")
    private String cardExistence;
    
    @JsonProperty("CardStatusID")
    private Integer cardStatusID;
    
    @JsonProperty("CardStatus")
    private String cardStatus;
    
    @JsonProperty("IsValidCard")
    private Boolean isValidCard;
    
    @JsonProperty("IsActive")
    private Boolean isActive;
    
    @JsonProperty("StatusDescription")
    private String statusDescription;
    
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
    
    @JsonProperty("PFNumber")
    private String pfNumber;
    
    @JsonProperty("DateOfBirth")
    private String dateOfBirth;
    
    @JsonProperty("YearOfBirth")
    private Integer yearOfBirth;
    
    @JsonProperty("Age")
    private String age;
    
    @JsonProperty("ExpiryDate")
    private String expiryDate;
    
    @JsonProperty("CHNationalID")
    private String chNationalID;
    
    @JsonProperty("AuthorizationStatus")
    private String authorizationStatus;
    
    @JsonProperty("AuthorizationNo")
    private String authorizationNo;
    
    @JsonProperty("TokenNo")
    private String tokenNo;
    
    @JsonProperty("Remarks")
    private String remarks;
    
    @JsonProperty("FacilityCode")
    private String facilityCode;
    
    @JsonProperty("ProductName")
    private String productName;
    
    @JsonProperty("ProductCode")
    private String productCode;
    
    @JsonProperty("CreatedBy")
    private String createdBy;
    
    @JsonProperty("VisitType")
    private String visitType;
    
    @JsonProperty("VisitMode")
    private String visitMode;
    
    @JsonProperty("EstimatedAmount")
    private String estimatedAmount;
    
    @JsonProperty("ClaimType")
    private String claimType;
    
    @JsonProperty("AuthorizationDate")
    private String authorizationDate;
    
    @JsonProperty("LastModifiedBy")
    private String lastModifiedBy;
    
    @JsonProperty("LastModifiedDate")
    private String lastModifiedDate;
    
    @JsonProperty("CreatedDate")
    private String createdDate;
    
    // Constructors
    public CardAuthorizationResponse() {}
    
    // Getters and setters
    public String getAuthorizationID() { return authorizationID; }
    public void setAuthorizationID(String authorizationID) { this.authorizationID = authorizationID; }
    
    public String getCardNo() { return cardNo; }
    public void setCardNo(String cardNo) { this.cardNo = cardNo; }
    
    public String getMembershipNo() { return membershipNo; }
    public void setMembershipNo(String membershipNo) { this.membershipNo = membershipNo; }
    
    public String getEmployerNo() { return employerNo; }
    public void setEmployerNo(String employerNo) { this.employerNo = employerNo; }
    
    public String getEmployerName() { return employerName; }
    public void setEmployerName(String employerName) { this.employerName = employerName; }
    
    public String getHasSupplementary() { return hasSupplementary; }
    public void setHasSupplementary(String hasSupplementary) { this.hasSupplementary = hasSupplementary; }
    
    public String getSupplementaryAgreementId() { return supplementaryAgreementId; }
    public void setSupplementaryAgreementId(String supplementaryAgreementId) { this.supplementaryAgreementId = supplementaryAgreementId; }
    
    public Integer getSchemeID() { return schemeID; }
    public void setSchemeID(Integer schemeID) { this.schemeID = schemeID; }
    
    public String getSchemeName() { return schemeName; }
    public void setSchemeName(String schemeName) { this.schemeName = schemeName; }
    
    public String getCardExistence() { return cardExistence; }
    public void setCardExistence(String cardExistence) { this.cardExistence = cardExistence; }
    
    public Integer getCardStatusID() { return cardStatusID; }
    public void setCardStatusID(Integer cardStatusID) { this.cardStatusID = cardStatusID; }
    
    public String getCardStatus() { return cardStatus; }
    public void setCardStatus(String cardStatus) { this.cardStatus = cardStatus; }
    
    public Boolean getIsValidCard() { return isValidCard; }
    public void setIsValidCard(Boolean isValidCard) { this.isValidCard = isValidCard; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public String getStatusDescription() { return statusDescription; }
    public void setStatusDescription(String statusDescription) { this.statusDescription = statusDescription; }
    
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
    
    public String getPfNumber() { return pfNumber; }
    public void setPfNumber(String pfNumber) { this.pfNumber = pfNumber; }
    
    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public Integer getYearOfBirth() { return yearOfBirth; }
    public void setYearOfBirth(Integer yearOfBirth) { this.yearOfBirth = yearOfBirth; }
    
    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }
    
    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
    
    public String getChNationalID() { return chNationalID; }
    public void setChNationalID(String chNationalID) { this.chNationalID = chNationalID; }
    
    public String getAuthorizationStatus() { return authorizationStatus; }
    public void setAuthorizationStatus(String authorizationStatus) { this.authorizationStatus = authorizationStatus; }
    
    public String getAuthorizationNo() { return authorizationNo; }
    public void setAuthorizationNo(String authorizationNo) { this.authorizationNo = authorizationNo; }
    
    public String getTokenNo() { return tokenNo; }
    public void setTokenNo(String tokenNo) { this.tokenNo = tokenNo; }
    
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    
    public String getFacilityCode() { return facilityCode; }
    public void setFacilityCode(String facilityCode) { this.facilityCode = facilityCode; }
    
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public String getVisitType() { return visitType; }
    public void setVisitType(String visitType) { this.visitType = visitType; }
    
    public String getVisitMode() { return visitMode; }
    public void setVisitMode(String visitMode) { this.visitMode = visitMode; }
    
    public String getEstimatedAmount() { return estimatedAmount; }
    public void setEstimatedAmount(String estimatedAmount) { this.estimatedAmount = estimatedAmount; }
    
    public String getClaimType() { return claimType; }
    public void setClaimType(String claimType) { this.claimType = claimType; }
    
    public String getAuthorizationDate() { return authorizationDate; }
    public void setAuthorizationDate(String authorizationDate) { this.authorizationDate = authorizationDate; }
    
    public String getLastModifiedBy() { return lastModifiedBy; }
    public void setLastModifiedBy(String lastModifiedBy) { this.lastModifiedBy = lastModifiedBy; }
    
    public String getLastModifiedDate() { return lastModifiedDate; }
    public void setLastModifiedDate(String lastModifiedDate) { this.lastModifiedDate = lastModifiedDate; }
    
    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
}