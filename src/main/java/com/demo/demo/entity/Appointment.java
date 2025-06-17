package com.demo.demo.entity;


import com.demo.demo.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    private long id;
    LocalDate createAt;
    AppointmentStatus status;

    @ManyToOne
    @JoinColumn(name = "consultant_id")
    Consultant consultant;


}
