package com.demo.demo.dto;

import lombok.Data;

import java.time.LocalDate;

@Data // Lombok sẽ tạo getters, setters, toString, v.v.
public class ConsultantResponse {
    private long id; // ID của Consultant entity
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
    private Long accountId;


    private String consultantName;

}