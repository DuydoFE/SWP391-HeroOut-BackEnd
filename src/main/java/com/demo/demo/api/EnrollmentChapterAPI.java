package com.demo.demo.api;

import com.demo.demo.entity.EnrollmentChapter;
import com.demo.demo.service.EnrollmentChapterService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollment-chapters")
@SecurityRequirement(name = "api")
@RequiredArgsConstructor
public class EnrollmentChapterAPI {
    private final EnrollmentChapterService service;

    @GetMapping
    public List<EnrollmentChapter> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentChapter> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public EnrollmentChapter create(@RequestParam Long enrollmentId, @RequestParam Long chapterId,
                                    @RequestBody EnrollmentChapter chapter) {
        return service.create(enrollmentId, chapterId, chapter);
    }

    @PutMapping("/{id}")
    public EnrollmentChapter update(@PathVariable Long id, @RequestBody EnrollmentChapter chapter) {
        return service.update(id, chapter);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
