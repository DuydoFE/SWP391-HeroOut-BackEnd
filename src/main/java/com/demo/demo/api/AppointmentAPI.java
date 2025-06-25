package com.demo.demo.api;


import com.demo.demo.dto.AppointmentRequest;
import com.demo.demo.dto.AppointmentResponse;
import com.demo.demo.entity.Appointment;
import com.demo.demo.enums.AppointmentStatus;
import com.demo.demo.service.AppointmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentAPI {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentResponse> create(@RequestBody @Valid AppointmentRequest request) {
        return ResponseEntity.ok(appointmentService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> getAll() {
        return ResponseEntity.ok(appointmentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AppointmentResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam AppointmentStatus status
    ) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        appointmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/checkin/{id}")
    public ResponseEntity<String> checkIn(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.checkInAppointment(id));
    }
}

