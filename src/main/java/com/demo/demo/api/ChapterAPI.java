package com.demo.demo.api;

import com.demo.demo.dto.ChapterRequest;
import com.demo.demo.dto.ChapterResponse;
import com.demo.demo.dto.ChapterResponseStatus;
import com.demo.demo.service.ChapterService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chapters")
@SecurityRequirement(name = "api")
public class ChapterAPI {

    @Autowired
    private ChapterService chapterService;

    @PutMapping("/{id}")
    public ChapterResponse updateChapter(@PathVariable Long id,
                                         @RequestBody ChapterRequest request,
                                         @RequestParam Long courseId) {
        return chapterService.updateChapter(id, request, courseId);
    }

    @GetMapping("/{id}")
    public ChapterResponse getChapterById(@PathVariable Long id) {
        return chapterService.getChapterById(id);
    }

    @GetMapping
    public List<ChapterResponse> getAllChapters() {
        return chapterService.getAllChapters();
    }

    @GetMapping("/course/{courseId}")
    public List<ChapterResponse> getChaptersByCourseId(@PathVariable Long courseId) {
        return chapterService.getChaptersByCourseIdWithoutAccount(courseId);
    }

    @GetMapping("/course/{courseId}/account/{accountId}")
    public List<ChapterResponseStatus> getChaptersByCourseIdAndAccountId(@PathVariable Long courseId, @PathVariable Long accountId) {
        return chapterService.getChaptersByCourseId(courseId, accountId);
    }

    @DeleteMapping("/{id}")
    public void deleteChapter(@PathVariable Long id) {
        chapterService.deleteChapter(id);
    }
}
