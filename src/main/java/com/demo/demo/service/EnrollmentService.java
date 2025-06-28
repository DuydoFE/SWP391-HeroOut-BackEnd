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
public class EnrollmentService {

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

    public CourseEnrollmentResponse enrollToCourse(Long courseId, Long accountId) {
        Optional<Enrollment> existingOpt = enrollmentRepository.findByCourse_IdAndAccount_Id(courseId, accountId);
        if (existingOpt.isPresent()) {
            return modelMapper.map(existingOpt.get(), CourseEnrollmentResponse.class);
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course);
        enrollment.setAccount(account);
        enrollment.setStatus(ProgressStatus.INPROGRESS);
        enrollment.setCreatedAt(LocalDateTime.now());

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        List<EnrollmentChapter> ecList = course.getChapters().stream().map(ch -> {
            EnrollmentChapter ec = new EnrollmentChapter();
            ec.setChapter(ch);
            ec.setEnrollment(savedEnrollment);
            ec.setAccount(account);
            ec.setStatus(ProgressStatus.INPROGRESS);
            return ec;
        }).collect(Collectors.toList());

        enrollmentChapterRepository.saveAll(ecList);

        return modelMapper.map(savedEnrollment, CourseEnrollmentResponse.class);
    }

    public void completeChapter(Long enrollmentChapterId) {
        EnrollmentChapter ec = enrollmentChapterRepository.findById(enrollmentChapterId)
                .orElseThrow(() -> new RuntimeException("EnrollmentChapter not found"));

        if (ec.getAccount() == null && ec.getEnrollment() != null) {
            ec.setAccount(ec.getEnrollment().getAccount());
        }

        ec.setStatus(ProgressStatus.COMPLETED);
        enrollmentChapterRepository.save(ec);

        Enrollment enrollment = ec.getEnrollment();
        List<EnrollmentChapter> allChapters = enrollmentChapterRepository.findByEnrollmentId(enrollment.getId());

        boolean allCompleted = allChapters.stream()
                .allMatch(ch -> ch.getStatus() == ProgressStatus.COMPLETED);

        if (allCompleted) {
            enrollment.setStatus(ProgressStatus.COMPLETED);
            enrollmentRepository.save(enrollment);
        }
    }

    public void completeChapterByChapterAndAccount(Long chapterId, Long accountId) {
        EnrollmentChapter ec = enrollmentChapterRepository
                .findByChapter_IdAndAccount_Id(chapterId, accountId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("EnrollmentChapter not found"));

        ec.setStatus(ProgressStatus.COMPLETED);
        enrollmentChapterRepository.save(ec);

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
