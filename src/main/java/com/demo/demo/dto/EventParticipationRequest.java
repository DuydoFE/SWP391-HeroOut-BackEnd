package com.demo.demo.dto;

import com.demo.demo.enums.EventParticipationStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Data
public class EventParticipationRequest {
    private Long accountId;
    private Long eventId;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private EventParticipationStatus status;
}
