package com.demo.demo.repository;

import com.demo.demo.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByCourse_IdAndAccount_Id(Long courseId, Long accountId);

    long countByCourse_IdAndAccountIsNotNull(Long courseId);
}
