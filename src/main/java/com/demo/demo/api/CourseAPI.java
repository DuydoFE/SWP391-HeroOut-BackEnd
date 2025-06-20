package com.demo.demo.api;

import com.demo.demo.entity.Course;
import com.demo.demo.service.CourseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@SecurityRequirement(name = "api")
@RequiredArgsConstructor
public class CourseAPI {
    private final CourseService service;

    @GetMapping
    public List<Course> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public Course create(@RequestBody Course course) {
        return service.create(course);
    }

    @PutMapping("/{id}")
    public Course update(@PathVariable Long id, @RequestBody Course course) {
        return service.update(id, course);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
