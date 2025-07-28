package com.demo.demo.repository;

import com.demo.demo.entity.SurveyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyResultRepository extends JpaRepository<SurveyResult, Long> {


    List<SurveyResult> findByAccountId(Long accountId);


}