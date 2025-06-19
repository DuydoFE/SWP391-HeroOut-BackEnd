package com.demo.demo.dto;

import com.demo.demo.enums.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDTO {
    private Long id;
    private Long accountId;
    private Long courseId;
    private LocalDateTime enrolledAt;
    private EnrollmentStatus status;
}

