package com.demo.demo.dto;

import com.demo.demo.enums.EventParticipationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventParticipationResponse {
    private Long id;
    private Long accountId;
    private Long eventId;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private EventParticipationStatus status;
}
