package com.demo.demo.dto;

import com.demo.demo.enums.AgeGroup;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CourseCreateResponse {
    private Long id;
    private String title;
    private String description;
    private String objectives;
    private String overview;
    private AgeGroup ageGroup;
    private LocalDateTime createdAt;
    private long totalEnrollment;
    private List<ChapterResponse> chapters;
}