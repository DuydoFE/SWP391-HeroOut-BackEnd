package com.demo.demo.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ScheduleRequest {
    private LocalDate date;
    private String recurrence;
    private Long slotId;
    private Long consultantId;
}