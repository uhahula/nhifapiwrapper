package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents NHIF patient details
 */
public class PatientDetails {
    @JsonProperty("memberNumber")
    private String memberNumber;
    
    @JsonProperty("cardNumber")
    private String cardNumber;
    
    @JsonProperty("fullName")
    private String fullName;
    
    @JsonProperty("dateOfBirth")
    private String dateOfBirth;
    
    @JsonProperty("gender")
    private String gender;
    
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("physicalAddress")
    private String physicalAddress;
    
    @JsonProperty("postalAddress")
    private String postalAddress;
    
    @JsonProperty("cardStatus")
    private String cardStatus;
    
    @JsonProperty("cardExpiryDate")
    private String cardExpiryDate;
    
    @JsonProperty("principalMember")
    private PrincipalMember principalMember;
    
    @JsonProperty("dependents")
    private List<Dependent> dependents;
    
    // Getters and setters
    public String getMemberNumber() { return memberNumber; }
    public void setMemberNumber(String memberNumber) { this.memberNumber = memberNumber; }
    
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhysicalAddress() { return physicalAddress; }
    public void setPhysicalAddress(String physicalAddress) { this.physicalAddress = physicalAddress; }
    
    public String getPostalAddress() { return postalAddress; }
    public void setPostalAddress(String postalAddress) { this.postalAddress = postalAddress; }
    
    public String getCardStatus() { return cardStatus; }
    public void setCardStatus(String cardStatus) { this.cardStatus = cardStatus; }
    
    public String getCardExpiryDate() { return cardExpiryDate; }
    public void setCardExpiryDate(String cardExpiryDate) { this.cardExpiryDate = cardExpiryDate; }
    
    public PrincipalMember getPrincipalMember() { return principalMember; }
    public void setPrincipalMember(PrincipalMember principalMember) { this.principalMember = principalMember; }
    
    public List<Dependent> getDependents() { return dependents; }
    public void setDependents(List<Dependent> dependents) { this.dependents = dependents; }
    
    public static class PrincipalMember {
        @JsonProperty("memberNumber")
        private String memberNumber;
        
        @JsonProperty("fullName")
        private String fullName;
        
        @JsonProperty("dateOfBirth")
        private String dateOfBirth;
        
        @JsonProperty("gender")
        private String gender;
        
        // Getters and setters
        public String getMemberNumber() { return memberNumber; }
        public void setMemberNumber(String memberNumber) { this.memberNumber = memberNumber; }
        
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        
        public String getDateOfBirth() { return dateOfBirth; }
        public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
        
        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }
    }
    
    public static class Dependent {
        @JsonProperty("memberNumber")
        private String memberNumber;
        
        @JsonProperty("fullName")
        private String fullName;
        
        @JsonProperty("dateOfBirth")
        private String dateOfBirth;
        
        @JsonProperty("gender")
        private String gender;
        
        @JsonProperty("relationship")
        private String relationship;
        
        // Getters and setters
        public String getMemberNumber() { return memberNumber; }
        public void setMemberNumber(String memberNumber) { this.memberNumber = memberNumber; }
        
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        
        public String getDateOfBirth() { return dateOfBirth; }
        public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
        
        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }
        
        public String getRelationship() { return relationship; }
        public void setRelationship(String relationship) { this.relationship = relationship; }
    }
}
