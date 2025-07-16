package com.demo.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class EventSurveySubmissionDTO {
    private Long eventId;
    private Long accountId;
    private List<EventSurveyAnswerDTO> answers;
}
