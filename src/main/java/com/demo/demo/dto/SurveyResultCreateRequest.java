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

    // Ghi chú: ID tài khoản KHÔNG còn trong phần thân yêu cầu.
    // Nó sẽ được lấy từ người dùng đã được xác thực.
}