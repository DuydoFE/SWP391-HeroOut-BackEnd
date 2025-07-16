package com.demo.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class EventSurveyQuestionDTO {
    private Long id;
    private String questionText;
    private List<EventSurveyOptionDTO> options;
}
