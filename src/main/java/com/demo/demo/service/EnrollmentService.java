package com.demo.demo.service;

import com.demo.demo.dto.CourseEnrollmentResponse;

public interface EnrollmentService {
    CourseEnrollmentResponse enrollToCourse(Long courseId, Long accountId);
    void completeChapter(Long enrollmentChapterId);
}
