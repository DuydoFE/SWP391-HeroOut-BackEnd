package com.demo.demo.dto;

import lombok.Data;

@Data
public class CourseRequest {
    private String title;
    private String description;
    private String objectives;
    private String overview;
    private String ageGroup;
}