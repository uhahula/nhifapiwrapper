package com.oau.nhif.client;

import com.oau.nhif.client.model.*;
import com.oau.nhif.exception.NhifApiException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Client interface for interacting with the NHIF API.
 * All methods return CompletableFuture for asynchronous operations.
 */
public interface NhifApiClient extends AutoCloseable {
    // Approval APIs
    CompletableFuture<CardDetails> getCardDetails(String cardNumber) throws NhifApiException;
    CompletableFuture<AuthorizationDetails> getAuthorizationDetails(String authorizationNumber) throws NhifApiException;
    CompletableFuture<AuthorizationResponse> authorizeCard(AuthorizationRequest request) throws NhifApiException;
    CompletableFuture<CardAuthorizationResponse> authorizeCardWithBiometric(CardAuthorizationRequest request) throws NhifApiException;
    CompletableFuture<CardAuthorizationResponse> authorizeCardSimple(String cardNumber, int visitTypeID) throws NhifApiException;
    CompletableFuture<CardAuthorizationResponse> authorizeCardSimple(SimpleCardAuthorizationRequest request) throws NhifApiException;
    CompletableFuture<CardVerification> verifyCard(String cardNumber) throws NhifApiException;
    CompletableFuture<PatientDetails> getPatientDetails(String cardNumber) throws NhifApiException;

    // History APIs
    CompletableFuture<List<PatientVisit>> getPreviousPatientVisits(String cardNumber) throws NhifApiException;
    CompletableFuture<MedicalHistory> getMedicalHistory(String cardNumber) throws NhifApiException;

    // Claim APIs
    CompletableFuture<ClaimTest> testClaim() throws NhifApiException;
    CompletableFuture<ClaimSubmissionResponse> submitFolio(FolioSubmissionRequest request) throws NhifApiException;
    CompletableFuture<SubmittedClaim> getSubmittedClaim(String claimId) throws NhifApiException;
    CompletableFuture<List<ClaimSubmission>> getSubmittedClaims(String facilityCode, int claimYear, int claimMonth) throws NhifApiException;
    CompletableFuture<Receipt> getReceipt(String claimId) throws NhifApiException;
    CompletableFuture<GetReceiptResponse> getReceipt(String facilityCode, int claimYear, int claimMonth, String folioNo) throws NhifApiException;
    CompletableFuture<ConfirmationResponse> sendConfirmationCode(String phoneNumber) throws NhifApiException;
    CompletableFuture<MonthlyClaimResponse> submitMonthlyClaim(MonthlyClaim request) throws NhifApiException;
    CompletableFuture<MonthlyClaimSubmissionResponse> submitMonthlyClaimSubmission(MonthlyClaimSubmission request) throws NhifApiException;

    // Reference Data APIs
    CompletableFuture<List<PointOfCare>> getPointsOfCare() throws NhifApiException;
    CompletableFuture<List<VisitType>> getVisitTypes() throws NhifApiException;
    CompletableFuture<List<Facility>> getFacilities() throws NhifApiException;
    CompletableFuture<List<Disease>> getDiseases() throws NhifApiException;
    
    // Extended Verification APIs
    CompletableFuture<CardDetails> getCardDetailsByNIN(String nationalId) throws NhifApiException;
    CompletableFuture<PercentCovered> getPercentCovered(String authorizationNo, String itemCode) throws NhifApiException;
    CompletableFuture<EligibilityCheck> checkEligibility(String cardNo, String itemCode) throws NhifApiException;

    // Admissions APIs
    CompletableFuture<List<AdmissionType>> getAdmissionTypes() throws NhifApiException;
    CompletableFuture<List<DischargeType>> getDischargeTypes() throws NhifApiException;
    CompletableFuture<List<WardType>> getWardTypes() throws NhifApiException;
    CompletableFuture<List<RoomType>> getRoomTypes() throws NhifApiException;
    CompletableFuture<GenericResponse> getDetailsByAuthorizationNo(String authorizationNo) throws NhifApiException;
    CompletableFuture<GenericResponse> getAdmissionDetailsByAuthorizationNo(String authorizationNo) throws NhifApiException;
    CompletableFuture<List<AdmissionType>> getAdmissionTypesByProductCode(String productCode) throws NhifApiException;
    CompletableFuture<GenericResponse> admitPatient(PatientAdmissionModel request) throws NhifApiException;
    CompletableFuture<GenericResponse> dischargePatient(PatientDischargeModel request) throws NhifApiException;
    CompletableFuture<GenericResponse> transferPatient(PatientTransferModel request) throws NhifApiException;
    CompletableFuture<List<AdmittedPatient>> getAdmittedPatients() throws NhifApiException;
    CompletableFuture<List<AdmittedPatient>> getAdmittedPatientsByFacility(String facilityCode) throws NhifApiException;

    // Packages APIs
    CompletableFuture<List<PackageItem>> getPackageItems() throws NhifApiException;
    CompletableFuture<List<ItemType>> getItemTypes() throws NhifApiException;
    CompletableFuture<List<BenefitScheme>> getBenefitSchemes() throws NhifApiException;
    CompletableFuture<List<PricePackage>> getPricePackages() throws NhifApiException;
    CompletableFuture<List<FacilityPackageItem>> getPricePackageByFacility(String facilityCode) throws NhifApiException;

    // Attendance APIs
    CompletableFuture<GenericResponse> loginPractitioner(PractitionerAttendanceRequest request) throws NhifApiException;
    CompletableFuture<GenericResponse> logoutPractitioner(String practitionerNo, String facilityCode) throws NhifApiException;

    // Token management
    String getCurrentToken();
    void refreshToken() throws NhifApiException;
    
    // Configuration
    NhifApiConfig getConfig();
    
    @Override
    void close();
}
