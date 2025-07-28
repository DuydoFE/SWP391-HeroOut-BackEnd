package com.demo.demo.service;

import com.demo.demo.entity.Account;
import com.demo.demo.entity.SurveyResult;
import com.demo.demo.dto.SurveyResultCreateRequest; // Import the DTO
import com.demo.demo.repository.AccountRepository;
import com.demo.demo.repository.SurveyResultRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Use Spring's Transactional

import java.time.LocalDateTime; // Use LocalDateTime
import java.time.LocalTime;
import java.util.List;

@Service
public class SurveyResultService {

    private final SurveyResultRepository surveyResultRepository;
    private final AccountRepository accountRepository; // Needed to link result to account

    @Autowired
    public SurveyResultService(SurveyResultRepository surveyResultRepository, AccountRepository accountRepository) {
        this.surveyResultRepository = surveyResultRepository;
        this.accountRepository = accountRepository;
    }


    public List<SurveyResult> getAllSurveyResults() {
        return surveyResultRepository.findAll();
    }


    public List<SurveyResult> getSurveyResultsByAccountId(Long accountId) {

        if (!accountRepository.existsById(accountId)) {
            throw new EntityNotFoundException("Account with ID " + accountId + " not found.");
        }
        return surveyResultRepository.findByAccountId(accountId);
    }

    // Method to create a new survey result from DTO and account ID
    @Transactional // Ensure the operation is transactional
    public SurveyResult createSurveyResult(SurveyResultCreateRequest request, Long accountId) {
        // 1. Find the Account entity
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account with ID " + accountId + " not found."));

        // 2. Create a new SurveyResult entity
        SurveyResult surveyResult = new SurveyResult();

        // 3. Map data from DTO and set other fields
        surveyResult.setAccount(account);
        surveyResult.setScore(request.getScore());
        surveyResult.setRiskLevel(request.getRiskLevel());
        surveyResult.setTakenAt(LocalTime.now()); // Set current timestamp


        return surveyResultRepository.save(surveyResult);
    }
}