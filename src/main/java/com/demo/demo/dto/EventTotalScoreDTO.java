package com.demo.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventTotalScoreDTO {
    private Long accountId;
    private String accountName;
    private Integer totalScore;
}
