package com.demo.demo.service.impl;

import com.demo.demo.dto.CourseRequest;
import com.demo.demo.dto.CourseResponse;
import com.demo.demo.entity.Course;
import com.demo.demo.repository.CourseRepository;
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
    private ModelMapper modelMapper;

    @Override
    public CourseResponse createCourse(CourseRequest request) {
        Course course = modelMapper.map(request, Course.class);
        course.setCreatedAt(LocalDateTime.now());
        return modelMapper.map(courseRepository.save(course), CourseResponse.class);
    }

    @Override
    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setObjectives(request.getObjectives());
        course.setOverview(request.getOverview());
        course.setAgeGroup(request.getAgeGroup());

        return modelMapper.map(courseRepository.save(course), CourseResponse.class);
    }

    @Override
    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return modelMapper.map(course, CourseResponse.class);
    }

    @Override
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(course -> modelMapper.map(course, CourseResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}
