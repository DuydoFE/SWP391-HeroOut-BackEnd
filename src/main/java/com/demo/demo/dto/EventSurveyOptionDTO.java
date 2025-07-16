package com.demo.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EventSurveyOptionDTO {
    private Long id;

    @NotBlank(message = "Option content is required")
    private String content;

    @NotNull(message = "Option score is required")
    private Integer score;
    private Long questionId;
}

