package com.demo.demo.service;

import com.demo.demo.dto.CourseRequest;
import com.demo.demo.dto.CourseResponse;
import com.demo.demo.enums.ProgressStatus;

import java.util.List;

public interface CourseService {

    CourseResponse createCourse(CourseRequest request);

    CourseResponse updateCourse(Long id, CourseRequest request);

    CourseResponse getCourseById(Long id);

    List<CourseResponse> getAllCourses();

    void deleteCourse(Long id);

    List<CourseResponse> getCoursesByStatusAndAccount(Long accountId, ProgressStatus status);

    List<CourseResponse> getCoursesNotStartedByAccount(Long accountId);

    long countEnrollmentsByCourseId(Long courseId);
}
