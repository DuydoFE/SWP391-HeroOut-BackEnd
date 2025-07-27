package com.demo.demo.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class ConsultantUpdateRequest {
    private String fieldOfStudy;
    private String degreeLevel;
    private LocalDate issuedDate;
    private LocalDate expiryDate;
    private String organization;
    private String specialities;
    private String experience;
    private float rating;
    private int consultations;
    private String bio;
}