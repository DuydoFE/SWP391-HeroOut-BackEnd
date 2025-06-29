package com.demo.demo.dto;

import com.demo.demo.enums.AppointmentStatus;
import lombok.Data;

import java.time.LocalDate;

@Data // Lombok will generate getters, setters, toString, equals, and hashCode
public class AppointmentResponse {

    private long id;
    private LocalDate createAt;
    private String description;
    private AppointmentStatus status;

    private Long accountId;
    private Long consultantId;
    private String meetingLink;
    private boolean checkedIn;



    private LocalDate appointmentDate;


}