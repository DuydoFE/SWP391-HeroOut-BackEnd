package com.demo.demo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class EventSurveyDTO {
    @NotNull(message = "Event ID is required")
    private Long eventId;

    @NotBlank(message = "Survey title is required")
    private String title;

    @Valid
    @NotEmpty(message = "Survey must have at least one question")
    private List<@Valid EventSurveyQuestionDTO> questions;
}
