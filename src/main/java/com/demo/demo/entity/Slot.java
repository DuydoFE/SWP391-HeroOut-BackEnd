package com.demo.demo.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Slot {
    @Id
    private long id;
    private LocalTime slot_start;
    private LocalTime slot_end;
    private boolean isBooked;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

}
