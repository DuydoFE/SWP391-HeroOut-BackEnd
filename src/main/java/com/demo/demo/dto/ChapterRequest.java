package com.demo.demo.dto;

import lombok.Data;

@Data
public class ChapterRequest {
    private String title;
    private String content;
    private String image;
    private String video;
}