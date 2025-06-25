package com.demo.demo.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ScheduleResponse {
    private long id;
    private LocalDate date;
    private String recurrence;
    private long consultantId;
    private long slotId;
    private long appointmentId;
}
