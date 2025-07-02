package com.demo.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponseDto {
    private long id;
    private LocalDate date;
    private String recurrence;


    private int bookedStatus;

    private Long slotId;

    private SlotDto slot;


    private Long consultantId;


}