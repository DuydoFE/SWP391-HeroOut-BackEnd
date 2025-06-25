package com.demo.demo.service;

import com.demo.demo.dto.CourseRequest;
import com.demo.demo.dto.CourseResponse;

import java.util.List;

public interface CourseService {
    CourseResponse createCourse(CourseRequest request);
    CourseResponse updateCourse(Long id, CourseRequest request);
    CourseResponse getCourseById(Long id);
    List<CourseResponse> getAllCourses();
    void deleteCourse(Long id);
}
