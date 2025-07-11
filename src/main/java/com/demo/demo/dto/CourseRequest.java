package com.demo.demo.dto;

import com.demo.demo.enums.AgeGroup;
import lombok.Data;

import java.util.List;

@Data
public class CourseRequest {
    private String title;
    private String description;
    private String objectives;
    private String overview;
    private AgeGroup ageGroup;
    private List<ChapterRequest> chapters;
}