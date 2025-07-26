package com.demo.demo.dto;

import lombok.Data;
import com.demo.demo.enums.ProgressStatus;

@Data
public class ChapterResponseStatus {
    private Long id;
    private Long courseId;
    private String title;
    private String content;
    private String image;
    private String video;
    private ProgressStatus status;
}

