package com.demo.demo.repository;

import com.demo.demo.entity.Course;
import com.demo.demo.enums.ProgressStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT e.course FROM Enrollment e WHERE e.account.id = :accountId AND e.status = :status")
    List<Course> findCoursesByAccountIdAndStatus(@Param("accountId") Long accountId, @Param("status") ProgressStatus status);

    @Query("SELECT c FROM Course c WHERE c.id NOT IN (SELECT e.course.id FROM Enrollment e WHERE e.account.id = :accountId)")
    List<Course> findCoursesNotEnrolledByAccount(@Param("accountId") Long accountId);
}