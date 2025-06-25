package com.demo.demo.dto;

import com.demo.demo.enums.ProgressStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourseEnrollmentResponse {
    private Long id;
    private Long accountId;
    private Long courseId;
    private ProgressStatus status;
    private LocalDateTime createdAt;
}
