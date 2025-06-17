package com.demo.demo.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    String label;
    LocalTime slot_start;
    LocalTime slot_end;
   boolean isDeleted = false;


    @OneToMany(mappedBy = "slot")
    @JsonIgnore
    private Schedule schedule;

}
