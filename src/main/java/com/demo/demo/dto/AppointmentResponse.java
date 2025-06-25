package com.demo.demo.dto;

import com.demo.demo.entity.Appointment;
import com.demo.demo.enums.AppointmentStatus;
import com.demo.demo.service.AppointmentService;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AppointmentResponse {
    private long id;
    private LocalDate createAt;
    private AppointmentStatus status;
    private long accountId;
    private long scheduleId;
}
