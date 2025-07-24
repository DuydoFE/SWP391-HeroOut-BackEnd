package com.demo.demo.api;

import com.demo.demo.dto.EventSurveyDTO;
import com.demo.demo.service.EventSurveyService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/surveys")
@SecurityRequirement(name = "api")
@RequiredArgsConstructor
public class EventSurveyAPI {

    private final EventSurveyService surveyService;

    @PostMapping
    public ResponseEntity<EventSurveyDTO> create(@Valid @RequestBody EventSurveyDTO dto) {
        return ResponseEntity.ok(surveyService.createSurvey(dto));
    }

    @PutMapping("/event/{eventId}")
    public ResponseEntity<EventSurveyDTO> update(@PathVariable Long eventId, @Valid @RequestBody EventSurveyDTO dto) {
        return ResponseEntity.ok(surveyService.updateSurvey(eventId, dto));
    }

    @GetMapping("/event/{eventId}/{accountId}")
    public ResponseEntity<EventSurveyDTO> getSurvey(
            @PathVariable Long eventId,
            @PathVariable Long accountId) {
        return ResponseEntity.ok(surveyService.getSurveyByEvent(eventId, accountId));
    }



    @DeleteMapping("/event/{eventId}")
    public ResponseEntity<Void> delete(@PathVariable Long eventId) {
        surveyService.deleteSurvey(eventId);
        return ResponseEntity.noContent().build();
    }
}