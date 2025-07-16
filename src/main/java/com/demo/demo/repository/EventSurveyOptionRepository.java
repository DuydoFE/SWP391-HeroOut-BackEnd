package com.demo.demo.repository;

import com.demo.demo.entity.EventSurveyOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventSurveyOptionRepository extends JpaRepository<EventSurveyOption, Long> {
    List<EventSurveyOption> findByQuestion_Id(Long questionId);
}
