package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents NHIF card details
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardDetails {
    // Getters and setters
    @JsonProperty("CardExistence")
    private String cardExistence;
    
    @JsonProperty("PackageID")
    private Integer packageId;
    
    @JsonProperty("ProductCode")
    private String productCode;
    
    @JsonProperty("CardNo")
    private String cardNo;
    
    @JsonProperty("MembershipNo")
    private String membershipNo;
    
    @JsonProperty("EmployerNo")
    private String employerNo;
    
    @JsonProperty("EmployerName")
    private String employerName;
    
    @JsonProperty("SchemeID")
    private Integer schemeId;
    
    @JsonProperty("SchemeName")
    private String schemeName;
    
    @JsonProperty("FirstName")
    private String firstName;
    
    @JsonProperty("MiddleName")
    private String middleName;
    
    @JsonProperty("LastName")
    private String lastName;
    
    @JsonProperty("FullName")
    private String fullName;
    
    @JsonProperty("PFNumber")
    private String pfNumber;
    
    @JsonProperty("Gender")
    private String gender;
    
    @JsonProperty("DateOfBirth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateOfBirth;
    
    @JsonProperty("Age")
    private Integer age;
    
    @JsonProperty("CHNationalID")
    private String chNationalId;
    
    @JsonProperty("ExpiryDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expiryDate;
    
    @JsonProperty("CardStatusID")
    private Integer cardStatusId;
    
    @JsonProperty("CardStatus")
    private String cardStatus;
    
    @JsonProperty("StatusDescription")
    private String statusDescription;
    
    @JsonProperty("IsActive")
    private Boolean isActive;
    
    @JsonProperty("IsValidCard")
    private Boolean isValidCard;
    
    @JsonProperty("LatestContribution")
    private String latestContribution;
    
    @JsonProperty("AuthorizationStatus")
    private String authorizationStatus;
    
    @JsonProperty("AuthorizationNo")
    private String authorizationNo;
    
    @JsonProperty("Remarks")
    private String remarks;
    
    @JsonProperty("LatestAuthorization")
    private String latestAuthorization;

}
