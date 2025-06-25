package com.demo.demo.dto;


import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AppointmentRequest {

    private long slotId;
    private long accountId;
    private LocalDate appointmentDate;

}
