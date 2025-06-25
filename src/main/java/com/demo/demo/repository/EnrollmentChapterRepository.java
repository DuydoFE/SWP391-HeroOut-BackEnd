package com.demo.demo.repository;

import com.demo.demo.entity.EnrollmentChapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentChapterRepository extends JpaRepository<EnrollmentChapter, Long> {
    List<EnrollmentChapter> findByEnrollmentId(Long enrollmentId);
}
