package com.demo.demo.service.impl;

import com.demo.demo.dto.ChapterRequest;
import com.demo.demo.dto.ChapterResponse;
import com.demo.demo.entity.Chapter;
import com.demo.demo.entity.Course;
import com.demo.demo.repository.ChapterRepository;
import com.demo.demo.repository.CourseRepository;
import com.demo.demo.service.ChapterService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChapterServiceImpl implements ChapterService {

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ChapterResponse createChapter(ChapterRequest request) {
        Chapter chapter = new Chapter();
        chapter.setTitle(request.getTitle());
        chapter.setContent(request.getContent());

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        chapter.setCourse(course);

        return modelMapper.map(chapterRepository.save(chapter), ChapterResponse.class);
    }

    @Override
    public ChapterResponse updateChapter(Long id, ChapterRequest request) {
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));

        chapter.setTitle(request.getTitle());
        chapter.setContent(request.getContent());

        if (!chapter.getCourse().getId().equals(request.getCourseId())) {
            Course course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            chapter.setCourse(course);
        }

        return modelMapper.map(chapterRepository.save(chapter), ChapterResponse.class);
    }

    @Override
    public ChapterResponse getChapterById(Long id) {
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));
        return modelMapper.map(chapter, ChapterResponse.class);
    }

    @Override
    public List<ChapterResponse> getAllChapters() {
        return chapterRepository.findAll().stream()
                .map(chapter -> modelMapper.map(chapter, ChapterResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ChapterResponse> getChaptersByCourseId(Long courseId) {
        return chapterRepository.findByCourseId(courseId).stream()
                .map(chapter -> modelMapper.map(chapter, ChapterResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteChapter(Long id) {
        chapterRepository.deleteById(id);
    }
}
