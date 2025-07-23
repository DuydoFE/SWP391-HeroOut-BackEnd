package com.demo.demo.repository;

import com.demo.demo.entity.EventSurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventSurveyResponseRepository extends JpaRepository<EventSurveyResponse, Long> {
    List<EventSurveyResponse> findByParticipationId(Long participationId);
}
