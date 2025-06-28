package com.demo.demo.dto;

import lombok.Data;

@Data
public class CheckInResponse {
    private Long appointmentId;
    private String meetingLink;
    private boolean checkedIn;
}
