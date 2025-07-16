package com.demo.demo.repository;

import com.demo.demo.entity.EventSurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventSurveyQuestionRepository extends JpaRepository<EventSurveyQuestion, Long> {
    List<EventSurveyQuestion> findByEventSurveyId(Long surveyId);
}
