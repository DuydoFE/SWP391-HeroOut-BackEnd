package com.demo.demo.service;

import com.demo.demo.dto.ChapterRequest;
import com.demo.demo.dto.ChapterResponse;

import java.util.List;

public interface ChapterService {
    ChapterResponse createChapter(ChapterRequest request);
    ChapterResponse updateChapter(Long id, ChapterRequest request);
    ChapterResponse getChapterById(Long id);
    List<ChapterResponse> getAllChapters();
    List<ChapterResponse> getChaptersByCourseId(Long courseId);
    void deleteChapter(Long id);
}
