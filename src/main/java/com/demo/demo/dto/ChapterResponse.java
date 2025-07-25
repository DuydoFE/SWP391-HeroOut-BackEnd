package com.demo.demo.dto;

import lombok.Data;

@Data
public class ChapterResponse {
    private Long id;
    private Long courseId;
    private String title;
    private String content;
}