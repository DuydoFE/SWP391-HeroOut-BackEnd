package com.demo.demo.service.impl;

import com.demo.demo.dto.CourseRequest;
import com.demo.demo.dto.CourseResponse;
import com.demo.demo.entity.Course;
import com.demo.demo.entity.Enrollment;
import com.demo.demo.enums.ProgressStatus;
import com.demo.demo.repository.CourseRepository;
import com.demo.demo.repository.EnrollmentRepository;
import com.demo.demo.service.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CourseResponse createCourse(CourseRequest request) {
        Course course = modelMapper.map(request, Course.class);
        course.setCreatedAt(LocalDateTime.now());
        Course saved = courseRepository.save(course);

        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(saved);
        enrollment.setAccount(null);
        enrollment.setStatus(ProgressStatus.NOT_STARTED);
        enrollment.setCreatedAt(LocalDateTime.now());
        enrollmentRepository.save(enrollment);

        return mapCourseWithTotal(saved);
    }

    @Override
    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        modelMapper.map(request, course);
        return mapCourseWithTotal(courseRepository.save(course));
    }

    @Override
    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return mapCourseWithTotal(course);
    }

    @Override
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::mapCourseWithTotal)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    @Override
    public List<CourseResponse> getCoursesByStatusAndAccount(Long accountId, ProgressStatus status) {
        List<Course> courses = courseRepository.findCoursesByAccountIdAndStatus(accountId, status);
        return courses.stream()
                .map(this::mapCourseWithTotal)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseResponse> getCoursesNotStartedByAccount(Long accountId) {
        List<Course> courses = courseRepository.findCoursesNotEnrolledByAccount(accountId);
        return courses.stream()
                .map(this::mapCourseWithTotal)
                .collect(Collectors.toList());
    }

    @Override
    public long countEnrollmentsByCourseId(Long courseId) {
        return enrollmentRepository.countByCourse_IdAndAccountIsNotNull(courseId);
    }

    private CourseResponse mapCourseWithTotal(Course course) {
        CourseResponse response = modelMapper.map(course, CourseResponse.class);
        long total = course.getEnrollments() == null ? 0 : course.getEnrollments().stream()
                .filter(e -> e.getAccount() != null)
                .count();
        response.setTotal(total);
        return response;
    }
}
