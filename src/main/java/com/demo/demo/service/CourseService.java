package com.demo.demo.service;

import com.demo.demo.entity.Chapter;
import com.demo.demo.entity.Course;
import com.demo.demo.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public Course findById(Long id) {
        return courseRepository.findById(id).orElseThrow();
    }

    public Course create(Course course) {
        if (course.getChapters() != null) {
            for (Chapter chapter : course.getChapters()) {
                chapter.setCourse(course); // BẮT BUỘC!
            }
        }
        return courseRepository.save(course);
    }


    public Course update(Long id, Course course) {
        course.setId(id);
        return courseRepository.save(course);
    }

    public void delete(Long id) {
        courseRepository.deleteById(id);
    }
}
