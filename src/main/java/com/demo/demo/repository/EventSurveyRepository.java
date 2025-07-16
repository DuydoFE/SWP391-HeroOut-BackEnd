package com.demo.demo.repository;

import com.demo.demo.entity.EventSurvey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventSurveyRepository extends JpaRepository<EventSurvey, Long> {
    Optional<EventSurvey> findByEventId(Long eventId);
    void deleteByEventId(Long eventId);
}
