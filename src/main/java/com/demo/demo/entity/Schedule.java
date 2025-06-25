package com.demo.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;



    LocalDate date;
    private String recurrence;
    boolean isBooked =true;

    @ManyToOne
    @JoinColumn(name = "consultant_id")
    Consultant consultant;


    @ManyToOne
    @JoinColumn(name = "slot_id")
    Slot slot;

    @OneToMany

            (mappedBy = "schedule")
    @JsonIgnore
    List<Appointment> appointments;

}
