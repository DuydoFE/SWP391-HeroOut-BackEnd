package com.demo.demo.api;

import com.demo.demo.dto.CourseRequest;
import com.demo.demo.dto.CourseResponse;
import com.demo.demo.dto.CourseCreateResponse;
import com.demo.demo.dto.InProgressCourseResponse;
import com.demo.demo.enums.ProgressStatus;
import com.demo.demo.enums.CourseStatus;
import com.demo.demo.service.CourseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@SecurityRequirement(name = "api")
public class CourseAPI {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public CourseCreateResponse createCourse(@ModelAttribute CourseRequest request) {
        return courseService.createCourse(request);
    }

    @PutMapping("/{id}")
    public CourseResponse updateCourse(@PathVariable Long id, @RequestBody CourseRequest request) {
        return courseService.updateCourse(id, request);
    }

    @GetMapping("/{id}")
    public CourseResponse getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    @GetMapping
    public List<CourseResponse> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/not-started")
    public List<CourseResponse> getNotStartedCourses(@RequestParam Long accountId) {
        return courseService.getCoursesNotStartedByAccount(accountId);
    }

    @GetMapping("/in-progress")
    public List<InProgressCourseResponse> getInProgressCourses(@RequestParam Long accountId) {
        return courseService.getCoursesByStatusAndAccount(accountId, ProgressStatus.INPROGRESS);
    }

    @GetMapping("/completed")
    public List<InProgressCourseResponse> getCompletedCourses(@RequestParam Long accountId) {
        return courseService.getCoursesByStatusAndAccount(accountId, ProgressStatus.COMPLETED);
    }


    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateCourseStatus(@PathVariable Long id, @RequestParam("status") String status) {
        try {
            courseService.updateCourseStatus(id, CourseStatus.valueOf(status.toUpperCase()));
            return ResponseEntity.ok("Cập nhật trạng thái thành công");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Trạng thái không hợp lệ");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}