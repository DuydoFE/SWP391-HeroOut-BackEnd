package com.demo.demo.api;

import com.demo.demo.dto.EventParticipationRequest;
import com.demo.demo.entity.EventParticipation;
import com.demo.demo.service.EventParticipationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participations")
public class EventParticipationAPI {

    @Autowired
    private EventParticipationService participationService;

    @GetMapping
    public ResponseEntity<List<EventParticipation>> getAll() {
        return ResponseEntity.ok(participationService.getAllParticipations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventParticipation> getById(@PathVariable Long id) {
        return ResponseEntity.ok(participationService.getParticipationById(id));
    }

    @PostMapping
    public ResponseEntity<EventParticipation> create(@RequestBody EventParticipationRequest request) {
        EventParticipation created = participationService.createParticipation(request);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventParticipation> update(@PathVariable Long id,
                                                     @RequestBody EventParticipationRequest request) {
        EventParticipation updated = participationService.updateParticipation(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        participationService.deleteParticipation(id);
        return ResponseEntity.noContent().build();
    }
}
