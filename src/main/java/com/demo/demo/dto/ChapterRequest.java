package com.demo.demo.dto;

import lombok.Data;

@Data
public class    ChapterRequest {
    private Long courseId;
    private String title;
    private String content;
}
