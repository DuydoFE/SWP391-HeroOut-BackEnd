package com.demo.demo.api;

import com.demo.demo.dto.AppointmentRequest;
import com.demo.demo.dto.AppointmentResponse;
import com.demo.demo.dto.CheckInResponse;
import com.demo.demo.entity.Appointment;
import com.demo.demo.enums.AppointmentStatus; // Import AppointmentStatus
import com.demo.demo.exception.exceptions.ResourceNotFoundException; // Import ResourceNotFoundException
import com.demo.demo.service.AppointmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // Import HttpStatus
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List; // Import List
import java.util.Arrays; // Import Arrays for error message

@RestController
@RequestMapping("/api/appointment")
@SecurityRequirement(name = "api")
public class AppointmentAPI {
    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity <Appointment> create(@RequestBody AppointmentRequest appointmentRequest){

        Appointment appointment = appointmentService.create(appointmentRequest);
        return ResponseEntity.ok(appointment);
    }

    @PostMapping("/{id}/check-in")
    public CheckInResponse checkIn(@PathVariable Long id) {
        return appointmentService.checkInAppointment(id);
    }


    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable("id") Long appointmentId) {
        AppointmentResponse appointmentResponse = appointmentService.getAppointmentById(appointmentId);
        return ResponseEntity.ok(appointmentResponse);
    }


    @GetMapping // Maps to /api/appointment (GET)
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments() {
        List<AppointmentResponse> appointmentResponses = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointmentResponses);
    }


    @GetMapping("/account/{accountId}") // Endpoint: GET /api/appointment/account/{accountId}
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByAccountId(@PathVariable Long accountId) {
        List<AppointmentResponse> appointmentResponses = appointmentService.getAppointmentsByAccountId(accountId);
        return ResponseEntity.ok(appointmentResponses);
    }


    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam("status") String status) {
        try {

            AppointmentStatus newStatus = AppointmentStatus.valueOf(status.toUpperCase());

            appointmentService.updateAppointmentStatus(id, newStatus);

            return ResponseEntity.ok("Appointment status updated successfully");

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body("Invalid status value: " + status + ". Valid values are: " + Arrays.toString(AppointmentStatus.values()));
        } catch (ResourceNotFoundException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating appointment status: " + e.getMessage());
        }
    }

}