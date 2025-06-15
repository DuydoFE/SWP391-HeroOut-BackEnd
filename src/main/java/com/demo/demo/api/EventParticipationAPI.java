package com.demo.demo.api;

import com.demo.demo.entity.EventParticipation;
import com.demo.demo.service.EventParticipationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event-participations")
public class EventParticipationAPI {

    @Autowired
    private EventParticipationService eventParticipationService;

    // Lấy danh sách tất cả
    @GetMapping
    public List<EventParticipation> getAll() {
        return eventParticipationService.getAllEventParticipations();
    }

    // Lấy theo ID
    @GetMapping("/{id}")
    public ResponseEntity<EventParticipation> getById(@PathVariable long id) {
        try {
            EventParticipation participation = eventParticipationService.getEventParticipationByID(id);
            return ResponseEntity.ok(participation);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Tạo mới
    @PostMapping
    public ResponseEntity<EventParticipation> create(@RequestBody EventParticipation participation) {
        EventParticipation saved = eventParticipationService.saveEventParticipation(participation);
        return ResponseEntity.ok(saved);
    }

    // Cập nhật
    @PutMapping("/{id}")
    public ResponseEntity<EventParticipation> update(
            @PathVariable long id,
            @RequestBody EventParticipation participation) {
        try {
            // Đảm bảo tồn tại trước khi cập nhật
            eventParticipationService.getEventParticipationByID(id);
            participation.setId(id); // Cập nhật đúng ID
            EventParticipation updated = eventParticipationService.updateEventParticipation(participation);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        try {
            eventParticipationService.deleteEventParticipation(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

}
