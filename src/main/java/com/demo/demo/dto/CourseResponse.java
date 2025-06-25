package com.demo.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CourseResponse {
    private Long id;
    private String title;
    private String description;
    private String objectives;
    private String overview;
    private String ageGroup;
    private LocalDateTime createdAt;
}

