package com.demo.demo.api;

import com.demo.demo.dto.EventSurveySubmissionDTO;
import com.demo.demo.service.EventSurveyResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/survey-responses")
@RequiredArgsConstructor
public class EventSurveyResponseAPI {

    private final EventSurveyResponseService surveyResponseService;

    @PostMapping("/submit")
    public ResponseEntity<Integer> submitSurvey(@RequestBody EventSurveySubmissionDTO dto) {
        int score = surveyResponseService.submitSurvey(dto);
        return ResponseEntity.ok(score);
    }
}