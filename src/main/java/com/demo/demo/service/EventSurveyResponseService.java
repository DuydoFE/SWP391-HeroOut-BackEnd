package com.demo.demo.service;

import com.demo.demo.dto.EventSurveyAnswerDTO;
import com.demo.demo.dto.EventSurveySubmissionDTO;
import com.demo.demo.entity.*;
import com.demo.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventSurveyResponseService {

    private final EventRepository eventRepo;
    private final AccountRepository accountRepo;
    private final EventParticipationRepository participationRepo;
    private final EventSurveyQuestionRepository questionRepo;
    private final EventSurveyOptionRepository optionRepo;
    private final EventSurveyResponseRepository responseRepo;

    public int submitSurvey(EventSurveySubmissionDTO dto) {
        Event event = eventRepo.findById(dto.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Account account = accountRepo.findById(dto.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        EventParticipation participation = participationRepo
                .findByEventAndAccount(event, account)
                .orElseGet(() -> participationRepo.save(new EventParticipation(event, account)));

        List<EventSurveyResponse> responses = new ArrayList<>();
        int totalScore = 0;

        for (EventSurveyAnswerDTO answer : dto.getAnswers()) {
            EventSurveyQuestion question = questionRepo.findById(answer.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Invalid question ID"));
            EventSurveyOption option = optionRepo.findById(answer.getSelectedOptionId())
                    .orElseThrow(() -> new RuntimeException("Invalid option ID"));

            if (!option.getQuestion().getId().equals(question.getId()))
                throw new RuntimeException("Option does not belong to question");

            EventSurveyResponse response = new EventSurveyResponse();
            response.setParticipation(participation);
            response.setQuestion(question);
            response.setSelectedOption(option);
            responses.add(response);

            totalScore += option.getScore();
        }

        responseRepo.saveAll(responses);
        participation.setTotalScore(totalScore);
        participationRepo.save(participation);

        return totalScore;
    }
}
