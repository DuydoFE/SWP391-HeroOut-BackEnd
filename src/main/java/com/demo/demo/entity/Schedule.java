package com.demo.demo.entity;

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

    @ManyToOne
    @JoinColumn(name = "consultant_id")
    private Consultant consultant;

    LocalDate date;
    private String recurrence;
    boolean isBooked =true;

    @ManyToOne
    @JoinColumn(name = "account_id")
    Account account;


    @ManyToOne
    @JoinColumn(name = "slot_id")
    Slot slot;

}
