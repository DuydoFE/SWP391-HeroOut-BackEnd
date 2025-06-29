package com.demo.demo.service;

import com.demo.demo.dto.ChapterRequest;
import com.demo.demo.dto.ChapterResponse;
import com.demo.demo.dto.ChapterResponseStatus;
import com.demo.demo.entity.Chapter;
import com.demo.demo.entity.Course;
import com.demo.demo.enums.ProgressStatus;
import com.demo.demo.repository.ChapterRepository;
import com.demo.demo.repository.CourseRepository;
import com.demo.demo.repository.EnrollmentChapterRepository;
import com.demo.demo.exception.exceptions.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChapterService {

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EnrollmentChapterRepository enrollmentChapterRepository;

    public ChapterResponse updateChapter(Long id, ChapterRequest request, Long courseId) {
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));

        chapter.setTitle(request.getTitle());
        chapter.setContent(request.getContent());

        if (!chapter.getCourse().getId().equals(courseId)) {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            chapter.setCourse(course);
        }

        return modelMapper.map(chapterRepository.save(chapter), ChapterResponse.class);
    }

    public ChapterResponse getChapterById(Long id) {
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));
        return modelMapper.map(chapter, ChapterResponse.class);
    }

    public List<ChapterResponse> getAllChapters() {
        return chapterRepository.findAll().stream()
                .map(chapter -> modelMapper.map(chapter, ChapterResponse.class))
                .collect(Collectors.toList());
    }

    public List<ChapterResponseStatus> getChaptersByCourseId(Long courseId, Long accountId) {
        List<ChapterResponseStatus> result = chapterRepository.findByCourseId(courseId).stream().map(chapter -> {
            ChapterResponseStatus response = modelMapper.map(chapter, ChapterResponseStatus.class);
            // Lấy status của chương cho account này
            response.setStatus(
                    enrollmentChapterRepository.findByChapter_IdAndAccount_Id(chapter.getId(), accountId)
                            .stream()
                            .findFirst()
                            .map(ec -> ec.getStatus())
                            .orElse(null)
            );
            return response;
        }).collect(Collectors.toList());
        boolean allNull = result.stream().allMatch(r -> r.getStatus() == null);
        if (allNull) {
            throw new BadRequestException("User has not enrolled in this course.");
        }
        return result;
    }

    public List<ChapterResponse> getChaptersByCourseIdWithoutAccount(Long courseId) {
        return chapterRepository.findByCourseId(courseId).stream()
                .map(chapter -> modelMapper.map(chapter, ChapterResponse.class))
                .collect(Collectors.toList());
    }

    public void deleteChapter(Long id) {
        chapterRepository.deleteById(id);
    }
}
