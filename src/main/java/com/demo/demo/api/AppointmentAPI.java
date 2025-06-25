package com.demo.demo.api;


import com.demo.demo.dto.AppointmentRequest;
import com.demo.demo.entity.Appointment;
import com.demo.demo.service.AppointmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{id}/checkin")
    public String checkIn(@PathVariable("id") Long appointmentId) {
        return appointmentService.checkInAppointment(appointmentId);
    }
}
