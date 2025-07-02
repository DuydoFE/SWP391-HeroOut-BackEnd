package com.demo.demo.service;

import com.demo.demo.entity.Chapter;
import com.demo.demo.entity.Enrollment;
import com.demo.demo.entity.EnrollmentChapter;
import com.demo.demo.repository.ChapterRepository;
import com.demo.demo.repository.EnrollmentChapterRepository;
import com.demo.demo.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentChapterService {
    private final EnrollmentChapterRepository repository;
    private final EnrollmentRepository enrollmentRepository;
    private final ChapterRepository chapterRepository;

    public List<EnrollmentChapter> findAll() {
        return repository.findAll();
    }

    public EnrollmentChapter findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public EnrollmentChapter create(Long enrollmentId, Long chapterId, EnrollmentChapter entity) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow();
        Chapter chapter = chapterRepository.findById(chapterId).orElseThrow();
        if (chapter.getCourse() == null || chapter.getCourse().getStatus() != com.demo.demo.enums.CourseStatus.ACTIVE) {
            throw new RuntimeException("You can only enroll in chapters of courses that are ACTIVE.");
        }
        entity.setEnrollment(enrollment);
        entity.setChapter(chapter);
        return repository.save(entity);
    }

    public EnrollmentChapter update(Long id, EnrollmentChapter entity) {
        EnrollmentChapter existing = repository.findById(id).orElseThrow();
        entity.setId(id);
        entity.setEnrollment(existing.getEnrollment());
        entity.setChapter(existing.getChapter());
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}