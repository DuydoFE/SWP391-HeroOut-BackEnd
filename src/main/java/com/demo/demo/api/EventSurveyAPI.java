package com.demo.demo.api;

import com.demo.demo.dto.EventSurveyDTO;
import com.demo.demo.service.EventSurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
public class EventSurveyAPI {

    private final EventSurveyService surveyService;

    @PostMapping
    public ResponseEntity<EventSurveyDTO> create(@RequestBody EventSurveyDTO dto) {
        return ResponseEntity.ok(surveyService.createSurvey(dto));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<EventSurveyDTO> getByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(surveyService.getSurveyByEvent(eventId));
    }

    @PutMapping("/event/{eventId}")
    public ResponseEntity<EventSurveyDTO> update(@PathVariable Long eventId, @RequestBody EventSurveyDTO dto) {
        return ResponseEntity.ok(surveyService.updateSurvey(eventId, dto));
    }

    @DeleteMapping("/event/{eventId}")
    public ResponseEntity<Void> delete(@PathVariable Long eventId) {
        surveyService.deleteSurvey(eventId);
        return ResponseEntity.noContent().build();
    }
}