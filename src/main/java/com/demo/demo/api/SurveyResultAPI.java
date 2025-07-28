package com.demo.demo.api;

import com.demo.demo.entity.SurveyResult;
import com.demo.demo.dto.SurveyResultCreateRequest;
import com.demo.demo.service.SurveyResultService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/survey-results")
@SecurityRequirement(name = "api")
public class SurveyResultAPI {

    private final SurveyResultService surveyResultService;

    @Autowired
    public SurveyResultAPI(SurveyResultService surveyResultService) { // Constructor name updated
        this.surveyResultService = surveyResultService;
    }

    @GetMapping
    public ResponseEntity<List<SurveyResult>> getAllSurveyResults() {
        List<SurveyResult> results = surveyResultService.getAllSurveyResults();
        return ResponseEntity.ok(results);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<SurveyResult>> getSurveyResultsByAccountId(@PathVariable Long accountId) {
        try {
            List<SurveyResult> results = surveyResultService.getSurveyResultsByAccountId(accountId);
            return ResponseEntity.ok(results);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Return 404 Not Found
        }
    }

    @PostMapping("/account/{accountId}") // Endpoint is now more specific
    public ResponseEntity<SurveyResult> createSurveyResult(
            @RequestBody SurveyResultCreateRequest request,
            @PathVariable Long accountId) { // Get accountId from the path


        try {
            // Pass the accountId from the URL directly to the service
            SurveyResult createdResult = surveyResultService.createSurveyResult(request, accountId);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdResult);
        } catch (EntityNotFoundException e) {
            // If the accountId is not found, it's a client error (bad request).
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}