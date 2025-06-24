package com.demo.demo.api;

import com.demo.demo.dto.EventParticipationRequest;
import com.demo.demo.dto.EventParticipationResponse;
import com.demo.demo.entity.EventParticipation;
import com.demo.demo.enums.EventParticipationStatus;
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
    public List<EventParticipationResponse> getAllParticipations() {
        return eventParticipationService.getAllParticipationResponses();
    }

    @GetMapping("/{id}")
    public EventParticipationResponse getParticipationById(@PathVariable Long id) {
        EventParticipation participation = eventParticipationService.getParticipationById(id);
        return eventParticipationService.toResponse(participation);
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

    @PutMapping("/participations/{id}/checkin")
    public EventParticipation checkIn(@PathVariable Long id) {
        return eventParticipationService.checkInParticipation(id);
    }

    @PutMapping("/participations/{id}/checkout")
    public EventParticipation checkOut(@PathVariable Long id) {
        return eventParticipationService.checkOutParticipation(id);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<EventParticipation>> getByAccount(@PathVariable Long accountId) {
        List<EventParticipation> participations = eventParticipationService.getAllEventParticipationByAccountId(accountId);
        return ResponseEntity.ok(participations);
    }

    @GetMapping("/{eventId}/registered")
    public ResponseEntity<List<EventParticipationResponse>> getRegisteredParticipations(
            @RequestParam("eventId") Long eventId) {
        List<EventParticipationResponse> list =
                eventParticipationService.getAllByEventIdAndStatus(eventId, EventParticipationStatus.REGISTERED);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{eventId}/checked-in")
    public ResponseEntity<List<EventParticipationResponse>> getCheckedInParticipations(
            @RequestParam("eventId") Long eventId) {
        List<EventParticipationResponse> list =
                eventParticipationService.getAllByEventIdAndStatus(eventId, EventParticipationStatus.CHECKED_IN);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{eventId}/checked-out")
    public ResponseEntity<List<EventParticipationResponse>> getCheckedOutParticipations(
            @RequestParam("eventId") Long eventId) {
        List<EventParticipationResponse> list =
                eventParticipationService.getAllByEventIdAndStatus(eventId, EventParticipationStatus.CHECKED_OUT);
        return ResponseEntity.ok(list);
    }

}
