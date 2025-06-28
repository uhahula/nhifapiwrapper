package com.oau.nhif.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents a patient's medical history
 */
public class MedicalHistory {
    @JsonProperty("memberNumber")
    private String memberNumber;
    
    @JsonProperty("fullName")
    private String fullName;
    
    @JsonProperty("dateOfBirth")
    private String dateOfBirth;
    
    @JsonProperty("gender")
    private String gender;
    
    @JsonProperty("bloodType")
    private String bloodType;
    
    @JsonProperty("allergies")
    private List<String> allergies;
    
    @JsonProperty("chronicConditions")
    private List<ChronicCondition> chronicConditions;
    
    @JsonProperty("surgeries")
    private List<Surgery> surgeries;
    
    @JsonProperty("medications")
    private List<Medication> medications;
    
    @JsonProperty("immunizations")
    private List<Immunization> immunizations;
    
    // Getters and setters
    public String getMemberNumber() { return memberNumber; }
    public void setMemberNumber(String memberNumber) { this.memberNumber = memberNumber; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getBloodType() { return bloodType; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }
    
    public List<String> getAllergies() { return allergies; }
    public void setAllergies(List<String> allergies) { this.allergies = allergies; }
    
    public List<ChronicCondition> getChronicConditions() { return chronicConditions; }
    public void setChronicConditions(List<ChronicCondition> chronicConditions) { this.chronicConditions = chronicConditions; }
    
    public List<Surgery> getSurgeries() { return surgeries; }
    public void setSurgeries(List<Surgery> surgeries) { this.surgeries = surgeries; }
    
    public List<Medication> getMedications() { return medications; }
    public void setMedications(List<Medication> medications) { this.medications = medications; }
    
    public List<Immunization> getImmunizations() { return immunizations; }
    public void setImmunizations(List<Immunization> immunizations) { this.immunizations = immunizations; }
    
    public static class ChronicCondition {
        @JsonProperty("condition")
        private String condition;
        
        @JsonProperty("diagnosedDate")
        private String diagnosedDate;
        
        @JsonProperty("status")
        private String status;
        
        @JsonProperty("notes")
        private String notes;
        
        // Getters and setters
        public String getCondition() { return condition; }
        public void setCondition(String condition) { this.condition = condition; }
        
        public String getDiagnosedDate() { return diagnosedDate; }
        public void setDiagnosedDate(String diagnosedDate) { this.diagnosedDate = diagnosedDate; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }
    
    public static class Surgery {
        @JsonProperty("procedure")
        private String procedure;
        
        @JsonProperty("date")
        private String date;
        
        @JsonProperty("facility")
        private String facility;
        
        @JsonProperty("surgeon")
        private String surgeon;
        
        @JsonProperty("notes")
        private String notes;
        
        // Getters and setters
        public String getProcedure() { return procedure; }
        public void setProcedure(String procedure) { this.procedure = procedure; }
        
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        
        public String getFacility() { return facility; }
        public void setFacility(String facility) { this.facility = facility; }
        
        public String getSurgeon() { return surgeon; }
        public void setSurgeon(String surgeon) { this.surgeon = surgeon; }
        
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }
    
    public static class Medication {
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("dosage")
        private String dosage;
        
        @JsonProperty("frequency")
        private String frequency;
        
        @JsonProperty("startDate")
        private String startDate;
        
        @JsonProperty("endDate")
        private String endDate;
        
        @JsonProperty("prescribingDoctor")
        private String prescribingDoctor;
        
        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getDosage() { return dosage; }
        public void setDosage(String dosage) { this.dosage = dosage; }
        
        public String getFrequency() { return frequency; }
        public void setFrequency(String frequency) { this.frequency = frequency; }
        
        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }
        
        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }
        
        public String getPrescribingDoctor() { return prescribingDoctor; }
        public void setPrescribingDoctor(String prescribingDoctor) { this.prescribingDoctor = prescribingDoctor; }
    }
    
    public static class Immunization {
        @JsonProperty("vaccine")
        private String vaccine;
        
        @JsonProperty("dateAdministered")
        private String dateAdministered;
        
        @JsonProperty("facility")
        private String facility;
        
        @JsonProperty("nextDoseDate")
        private String nextDoseDate;
        
        // Getters and setters
        public String getVaccine() { return vaccine; }
        public void setVaccine(String vaccine) { this.vaccine = vaccine; }
        
        public String getDateAdministered() { return dateAdministered; }
        public void setDateAdministered(String dateAdministered) { this.dateAdministered = dateAdministered; }
        
        public String getFacility() { return facility; }
        public void setFacility(String facility) { this.facility = facility; }
        
        public String getNextDoseDate() { return nextDoseDate; }
        public void setNextDoseDate(String nextDoseDate) { this.nextDoseDate = nextDoseDate; }
    }
}
