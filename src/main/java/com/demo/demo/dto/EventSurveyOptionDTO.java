package com.demo.demo.dto;

import lombok.Data;

@Data
public class EventSurveyOptionDTO {
    private Long id;
    private String content;
    private int score;
    private Long questionId;
}

