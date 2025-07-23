package com.demo.demo.api;

import com.demo.demo.dto.EventSurveyQuestionDTO;
import com.demo.demo.service.EventSurveyQuestionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/questions")
@SecurityRequirement(name = "api")
@RequiredArgsConstructor
public class EventSurveyQuestionAPI {

    private final EventSurveyQuestionService questionService;



    @PostMapping("/survey/{surveyId}")
    public ResponseEntity<EventSurveyQuestionDTO> create(@PathVariable Long surveyId,
                                                         @Valid @RequestBody EventSurveyQuestionDTO dto) {
        return ResponseEntity.ok(questionService.createQuestion(dto, surveyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventSurveyQuestionDTO> update(@PathVariable Long id,
                                                         @Valid @RequestBody EventSurveyQuestionDTO dto) {
        return ResponseEntity.ok(questionService.updateQuestion(id, dto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventSurveyQuestionDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.getQuestion(id));
    }

    @GetMapping("/survey/{surveyId}")
    public ResponseEntity<List<EventSurveyQuestionDTO>> getBySurvey(@PathVariable Long surveyId) {
        return ResponseEntity.ok(questionService.getQuestionsBySurvey(surveyId));
    }
}
