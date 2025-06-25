package com.demo.demo.service;

import com.demo.demo.dto.CourseEnrollmentResponse;
import com.demo.demo.entity.*;
import com.demo.demo.enums.ProgressStatus;
import com.demo.demo.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private EnrollmentChapterRepository enrollmentChapterRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CourseEnrollmentResponse enrollToCourse(Long courseId, Long accountId) {
        // Check if already enrolled
        Optional<Enrollment> existingOpt = enrollmentRepository.findByCourse_IdAndAccount_Id(courseId, accountId);
        if (existingOpt.isPresent()) {
            return modelMapper.map(existingOpt.get(), CourseEnrollmentResponse.class);
        }

        // Get course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Get account
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Create new Enrollment
        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course);
        enrollment.setAccount(account);
        enrollment.setStatus(ProgressStatus.INPROGRESS);
        enrollment.setCreatedAt(LocalDateTime.now());

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        // Create related EnrollmentChapters
        List<EnrollmentChapter> ecList = course.getChapters().stream().map(ch -> {
            EnrollmentChapter ec = new EnrollmentChapter();
            ec.setChapter(ch);
            ec.setEnrollment(savedEnrollment);
            ec.setStatus(ProgressStatus.INPROGRESS);
            return ec;
        }).collect(Collectors.toList());

        enrollmentChapterRepository.saveAll(ecList);

        return modelMapper.map(savedEnrollment, CourseEnrollmentResponse.class);
    }

    @Override
    public void completeChapter(Long enrollmentChapterId) {
        // Find chapter
        EnrollmentChapter ec = enrollmentChapterRepository.findById(enrollmentChapterId)
                .orElseThrow(() -> new RuntimeException("EnrollmentChapter not found"));

        // Mark as completed
        ec.setStatus(ProgressStatus.COMPLETED);
        enrollmentChapterRepository.save(ec);

        // Check if all chapters completed
        Enrollment enrollment = ec.getEnrollment();
        List<EnrollmentChapter> allChapters = enrollmentChapterRepository.findByEnrollmentId(enrollment.getId());

        boolean allCompleted = allChapters.stream()
                .allMatch(ch -> ch.getStatus() == ProgressStatus.COMPLETED);

        if (allCompleted) {
            enrollment.setStatus(ProgressStatus.COMPLETED);
            enrollmentRepository.save(enrollment);
        }
    }
}
