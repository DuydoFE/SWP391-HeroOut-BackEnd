package com.demo.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResultCreateRequest {
    private int score;
    private String riskLevel;

    // Note: Account ID is NOT in the request body here.
    // It's assumed to be derived from the authenticated user
    // and passed separately to the service method.
}