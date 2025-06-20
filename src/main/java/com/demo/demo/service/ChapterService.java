package com.demo.demo.service;

import com.demo.demo.dto.ChapterDTO;
import com.demo.demo.entity.Chapter;
import com.demo.demo.entity.Course;
import com.demo.demo.repository.ChapterRepository;
import com.demo.demo.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChapterService {
    private final ChapterRepository chapterRepository;
    private final CourseRepository courseRepository;

    public List<Chapter> findAll() {
        return chapterRepository.findAll();
    }

    public Chapter findById(Long id) {
        return chapterRepository.findById(id).orElseThrow();
    }

    public Chapter create(Chapter chapter) {
        return chapterRepository.save(chapter);
    }

    public Chapter update(Long id, ChapterDTO dto) {
        Chapter chapter = chapterRepository.findById(id).orElseThrow();

        chapter.setTitle(dto.getTitle());
        chapter.setContent(dto.getContent());
        chapter.setSequence(dto.getSequence());

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        chapter.setCourse(course);

        return chapterRepository.save(chapter);
    }


    public void delete(Long id) {
        chapterRepository.deleteById(id);
    }
}
