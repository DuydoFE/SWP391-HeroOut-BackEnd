package com.demo.demo.dto;

import com.demo.demo.enums.EventParticipationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventParticipationRequest {
    private Long eventId;
    private Long memberId;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private EventParticipationStatus status;
}