package com.demo.demo.api;

import com.demo.demo.dto.EventDTO;
import com.demo.demo.dto.EventRequest;
import com.demo.demo.entity.Event;
import com.demo.demo.service.EventService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@SecurityRequirement(name = "api")
public class EventAPI {

    @Autowired
    private EventService eventService;

    // CREATE
    @PostMapping
    public ResponseEntity<?> createEvent(@Valid @RequestBody EventRequest request) {
        Event event = eventService.createEvent(request);
        return ResponseEntity.ok(event);
    }



    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<EventDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    // READ ONE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        EventDTO event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }


    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventRequest request
    ) {
        Event updatedEvent = eventService.updateEvent(id, request);
        return ResponseEntity.ok(updatedEvent);
    }


    // DELETE BY ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventById(@PathVariable Long id) {
        eventService.removeEventById(id);
        return ResponseEntity.noContent().build();
    }
}
