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
    CompletableFuture<CardVerification> verifyCard(String cardNumber) throws NhifApiException;
    CompletableFuture<PatientDetails> getPatientDetails(String cardNumber) throws NhifApiException;

    // History APIs
    CompletableFuture<List<PatientVisit>> getPreviousPatientVisits(String cardNumber) throws NhifApiException;
    CompletableFuture<MedicalHistory> getMedicalHistory(String cardNumber) throws NhifApiException;

    // Claim APIs
    CompletableFuture<TestResponse> testClaim() throws NhifApiException;
    CompletableFuture<ClaimSubmissionResponse> submitFolio(FolioSubmission request) throws NhifApiException;
    CompletableFuture<SubmittedClaim> getSubmittedClaim(String claimId) throws NhifApiException;
    CompletableFuture<Receipt> getReceipt(String claimId) throws NhifApiException;
    CompletableFuture<ConfirmationResponse> sendConfirmationCode(String phoneNumber) throws NhifApiException;
    CompletableFuture<MonthlyClaimResponse> submitMonthlyClaim(MonthlyClaim request) throws NhifApiException;

    // Reference Data APIs
    CompletableFuture<List<PointOfCare>> getPointsOfCare() throws NhifApiException;
    CompletableFuture<List<VisitType>> getVisitTypes() throws NhifApiException;
    CompletableFuture<List<Facility>> getFacilities() throws NhifApiException;
    
    // Extended Verification APIs
    CompletableFuture<CardDetails> getCardDetailsByNIN(String nationalId) throws NhifApiException;
    CompletableFuture<PercentCovered> getPercentCovered(String authorizationNo, String itemCode) throws NhifApiException;
    CompletableFuture<EligibilityCheck> checkEligibility(String cardNo, String itemCode) throws NhifApiException;

    // Token management
    String getCurrentToken();
    void refreshToken() throws NhifApiException;
    
    // Configuration
    NhifApiConfig getConfig();
    
    @Override
    void close();
}
