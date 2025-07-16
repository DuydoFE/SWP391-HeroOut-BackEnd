package com.demo.demo.api;

import com.demo.demo.dto.EventSurveyOptionDTO;
import com.demo.demo.service.EventSurveyOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/options")
@RequiredArgsConstructor
public class EventSurveyOptionAPI {

    private final EventSurveyOptionService optionService;

    @PostMapping
    public ResponseEntity<EventSurveyOptionDTO> create(@RequestBody EventSurveyOptionDTO dto) {
        return ResponseEntity.ok(optionService.createOption(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventSurveyOptionDTO> update(@PathVariable Long id, @RequestBody EventSurveyOptionDTO dto) {
        return ResponseEntity.ok(optionService.updateOption(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        optionService.deleteOption(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventSurveyOptionDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(optionService.getOption(id));
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<EventSurveyOptionDTO>> getByQuestion(@PathVariable Long questionId) {
        return ResponseEntity.ok(optionService.getOptionsByQuestion(questionId));
    }
}
