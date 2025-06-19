package com.demo.demo.api;

import com.demo.demo.entity.Enrollment;
import com.demo.demo.service.EnrollmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@SecurityRequirement(name = "api")
@RequiredArgsConstructor
public class EnrollmentAPI {
    private final EnrollmentService service;

    @GetMapping
    public List<Enrollment> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public Enrollment create(@RequestParam Long accountId, @RequestParam Long courseId,
                             @RequestBody Enrollment enrollment) {
        return service.create(accountId, courseId, enrollment);
    }

    @PutMapping("/{id}")
    public Enrollment update(@PathVariable Long id, @RequestBody Enrollment enrollment) {
        return service.update(id, enrollment);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
