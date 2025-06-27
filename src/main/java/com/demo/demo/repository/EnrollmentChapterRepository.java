package com.demo.demo.repository;

import com.demo.demo.entity.EnrollmentChapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentChapterRepository extends JpaRepository<EnrollmentChapter, Long> {

    List<EnrollmentChapter> findByEnrollmentId(Long enrollmentId);

    List<EnrollmentChapter> findByChapter_IdAndAccount_Id(Long chapterId, Long accountId);
}
