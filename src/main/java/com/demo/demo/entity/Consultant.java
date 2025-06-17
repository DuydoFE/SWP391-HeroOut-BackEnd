package com.demo.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Consultant {
    @Id
    @GeneratedValue
    private long id;
    private String fieldOfStudy;
    private String degreeLevel;
    private LocalDate issuedDate;
    private LocalDate expiryDate;
    private String organization;
    private String specialities;
    private String experence;
    private float rating;
    private int consultations;
    private String bio;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "consultant")
    private List<Schedule> schedules;

}
