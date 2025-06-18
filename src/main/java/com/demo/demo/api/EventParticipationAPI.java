package com.demo.demo.api;

import com.demo.demo.dto.EventParticipationRequest;
import com.demo.demo.entity.EventParticipation;
import com.demo.demo.service.EventParticipationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participations")
@SecurityRequirement(name = "api")

public class EventParticipationAPI {

    @Autowired
    private EventParticipationService eventParticipationService;

    @GetMapping
    public ResponseEntity<List<EventParticipation>> getAll() {
        return ResponseEntity.ok(eventParticipationService.getAllParticipations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventParticipation> getById(@PathVariable Long id) {
        return ResponseEntity.ok(eventParticipationService.getParticipationById(id));
    }

    @PostMapping
    public ResponseEntity<EventParticipation> create(@RequestBody EventParticipationRequest request) {
        EventParticipation created = eventParticipationService.createParticipation(request);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventParticipation> update(@PathVariable Long id,
                                                     @RequestBody EventParticipationRequest request) {
        EventParticipation updated = eventParticipationService.updateParticipation(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventParticipationService.deleteParticipation(id);
        return ResponseEntity.noContent().build();
    }
}
