package com.demo.demo.dto;

import com.demo.demo.enums.EventParticipationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventParticipationRequest {

    @NotNull(message = "Account ID is required")
    private Long accountId;

    @NotNull(message = "Event ID is required")
    private Long eventId;

    private LocalDateTime checkInTime;

    private LocalDateTime checkOutTime;

    private EventParticipationStatus status;
}
