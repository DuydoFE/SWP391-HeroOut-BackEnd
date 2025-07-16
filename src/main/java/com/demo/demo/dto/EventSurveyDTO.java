package com.demo.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class EventSurveyDTO {
    private Long eventId;
    private String title;
    private List<EventSurveyQuestionDTO> questions;
}
