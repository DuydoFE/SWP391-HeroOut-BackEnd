package com.demo.demo.service;

import com.demo.demo.entity.Account;
import com.demo.demo.entity.Course;
import com.demo.demo.entity.Enrollment;
import com.demo.demo.repository.AccountRepository;
import com.demo.demo.repository.CourseRepository;
import com.demo.demo.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final AccountRepository accountRepository;
    private final CourseRepository courseRepository;

    public List<Enrollment> findAll() {
        return enrollmentRepository.findAll();
    }

    public Enrollment findById(Long id) {
        return enrollmentRepository.findById(id).orElseThrow();
    }

    public Enrollment create(Long accountId, Long courseId, Enrollment enrollment) {
        Account account = accountRepository.findById(accountId).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();
        enrollment.setAccount(account);
        enrollment.setCourse(course);
        return enrollmentRepository.save(enrollment);
    }

    public Enrollment update(Long id, Enrollment enrollment) {
        Enrollment existing = enrollmentRepository.findById(id).orElseThrow();
        enrollment.setId(id);
        enrollment.setAccount(existing.getAccount());
        enrollment.setCourse(existing.getCourse());
        return enrollmentRepository.save(enrollment);
    }

    public void delete(Long id) {
        enrollmentRepository.deleteById(id);
    }
}

