package com.demo.demo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class EventSurveyQuestionDTO {

    private Long id;

    @NotBlank(message = "Question text is required")
    private String questionText;

    @Valid
    @NotEmpty(message = "Each question must have at least one option")
    private List<@Valid EventSurveyOptionDTO> options;
}
