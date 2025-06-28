package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents NHIF authorization details
 */
public class AuthorizationDetails {
    @JsonProperty("authorizationNumber")
    private String authorizationNumber;
    
    @JsonProperty("memberNumber")
    private String memberNumber;
    
    @JsonProperty("memberName")
    private String memberName;
    
    @JsonProperty("cardNumber")
    private String cardNumber;
    
    @JsonProperty("authorizationDate")
    private String authorizationDate;
    
    @JsonProperty("expiryDate")
    private String expiryDate;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("approvedAmount")
    private double approvedAmount;
    
    @JsonProperty("approvedItems")
    private List<ApprovedItem> approvedItems;
    
    @JsonProperty("facility")
    private Facility facility;
    
    @JsonProperty("diagnosis")
    private String diagnosis;
    
    @JsonProperty("remarks")
    private String remarks;
    
    // Getters and setters
    public String getAuthorizationNumber() { return authorizationNumber; }
    public void setAuthorizationNumber(String authorizationNumber) { this.authorizationNumber = authorizationNumber; }
    
    public String getMemberNumber() { return memberNumber; }
    public void setMemberNumber(String memberNumber) { this.memberNumber = memberNumber; }
    
    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }
    
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    
    public String getAuthorizationDate() { return authorizationDate; }
    public void setAuthorizationDate(String authorizationDate) { this.authorizationDate = authorizationDate; }
    
    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public double getApprovedAmount() { return approvedAmount; }
    public void setApprovedAmount(double approvedAmount) { this.approvedAmount = approvedAmount; }
    
    public List<ApprovedItem> getApprovedItems() { return approvedItems; }
    public void setApprovedItems(List<ApprovedItem> approvedItems) { this.approvedItems = approvedItems; }
    
    public Facility getFacility() { return facility; }
    public void setFacility(Facility facility) { this.facility = facility; }
    
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    
    public static class ApprovedItem {
        @JsonProperty("itemCode")
        private String itemCode;
        
        @JsonProperty("description")
        private String description;
        
        @JsonProperty("quantity")
        private int quantity;
        
        @JsonProperty("unitPrice")
        private double unitPrice;
        
        @JsonProperty("totalPrice")
        private double totalPrice;
        
        // Getters and setters
        public String getItemCode() { return itemCode; }
        public void setItemCode(String itemCode) { this.itemCode = itemCode; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        
        public double getUnitPrice() { return unitPrice; }
        public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
        
        public double getTotalPrice() { return totalPrice; }
        public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    }
    
    public static class Facility {
        @JsonProperty("code")
        private String code;
        
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("type")
        private String type;
        
        // Getters and setters
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }
}
