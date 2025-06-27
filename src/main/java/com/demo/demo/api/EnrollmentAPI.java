package com.demo.demo.api;

import com.demo.demo.dto.CourseEnrollmentResponse;
import com.demo.demo.service.EnrollmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enrollments")
@SecurityRequirement(name = "api")
public class EnrollmentAPI {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/start")
    public CourseEnrollmentResponse enrollToCourse(@RequestParam Long courseId, @RequestParam Long accountId) {
        return enrollmentService.enrollToCourse(courseId, accountId);
    }

    @PostMapping("/complete-chapter")
    public ResponseEntity<?> completeChapterByChapterAndAccount(
            @RequestParam Long chapterId,
            @RequestParam Long accountId) {
        enrollmentService.completeChapterByChapterAndAccount(chapterId, accountId);
        return ResponseEntity.ok("Chapter completed.");
    }
}