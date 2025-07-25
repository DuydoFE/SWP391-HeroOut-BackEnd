package com.demo.demo.service;

import com.demo.demo.dto.EventSurveyDTO;
import com.demo.demo.dto.EventSurveyOptionDTO;
import com.demo.demo.dto.EventSurveyQuestionDTO;
import com.demo.demo.entity.*;
import com.demo.demo.enums.EventParticipationStatus;
import com.demo.demo.enums.Role;
import com.demo.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventSurveyService {
    private final EventRepository eventRepo;
    private final EventSurveyRepository surveyRepo;
    private final EventSurveyQuestionRepository questionRepo;
    private final EventSurveyOptionRepository optionRepo;
    private final EventParticipationRepository eventParticipationRepository;
    private final AccountRepository accountRepository;
    private final EventSurveyQuestionService questionService;
    private final EventSurveyOptionService optionService;

    public EventSurveyDTO createSurvey(EventSurveyDTO dto) {
        Event event = eventRepo.findById(dto.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        EventSurvey survey = new EventSurvey();
        survey.setTitle(dto.getTitle());
        survey.setEvent(event); // chiều A → B
        event.setEventSurvey(survey); // ❗ PHẢI có chiều B → A để tránh Hibernate hiểu là entity mới

        List<EventSurveyQuestion> questions = new ArrayList<>();
        for (EventSurveyQuestionDTO qDto : dto.getQuestions()) {
            EventSurveyQuestion q = new EventSurveyQuestion();
            q.setQuestionText(qDto.getQuestionText());
            q.setEventSurvey(survey);

            List<EventSurveyOption> opts = new ArrayList<>();
            for (EventSurveyOptionDTO oDto : qDto.getOptions()) {
                EventSurveyOption o = new EventSurveyOption();
                o.setContent(oDto.getContent());
                o.setScore(oDto.getScore());
                o.setQuestion(q);
                opts.add(o);
            }
            q.setOptions(opts);
            questions.add(q);
        }

        survey.setQuestions(questions);

        return mapSurveyToDTO(surveyRepo.save(survey)); // Hibernate hiểu đúng vì đã có liên kết đầy đủ
    }

public EventSurveyDTO getSurveyByEvent(Long eventId, Long accountId) {
    Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new RuntimeException("You must login before accessing the survey."));

    if (account.getRole() == Role.MEMBER) {
        boolean isCheckedIn = eventParticipationRepository
                .existsByEvent_IdAndAccount_IdAndStatus(eventId, accountId, EventParticipationStatus.CHECKED_IN);

        if (!isCheckedIn) {
            throw new RuntimeException("You must check-in before accessing the survey.");
        }
    }

    EventSurvey survey = surveyRepo.findByEventId(eventId)
            .orElseThrow(() -> new RuntimeException("Survey not found"));

    return mapSurveyToDTO(survey);
}






    @Transactional
    public EventSurveyDTO updateSurvey(Long eventId, EventSurveyDTO dto) {
        EventSurvey survey = surveyRepo.findByEventId(eventId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        // Cập nhật title
        survey.setTitle(dto.getTitle());

        // Gọi service cập nhật từng câu hỏi
        for (EventSurveyQuestionDTO qDto : dto.getQuestions()) {
            if (qDto.getId() != null) {
                questionService.updateQuestion(qDto.getId(), qDto);
            }
        }

        return mapSurveyToDTO(surveyRepo.save(survey));
    }





    @Transactional
    public void deleteSurvey(Long eventId) {
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        EventSurvey survey = surveyRepo.findByEventId(eventId)
                .orElse(null);

        if (survey != null) {
            event.setEventSurvey(null);

            surveyRepo.delete(survey);

            eventRepo.save(event);
        }
    }


    private EventSurveyDTO mapSurveyToDTO(EventSurvey survey) {
        EventSurveyDTO dto = new EventSurveyDTO();
        dto.setEventId(survey.getEvent().getId());
        dto.setTitle(survey.getTitle());

        dto.setQuestions(survey.getQuestions().stream().map(q -> {
            EventSurveyQuestionDTO qDto = new EventSurveyQuestionDTO();
            qDto.setId(q.getId());
            qDto.setQuestionText(q.getQuestionText());

            qDto.setOptions(q.getOptions().stream().map(o -> {
                EventSurveyOptionDTO oDto = new EventSurveyOptionDTO();
                oDto.setId(o.getId());
                oDto.setContent(o.getContent());
                oDto.setScore(o.getScore());
                oDto.setQuestionId(q.getId());
                return oDto;
            }).collect(Collectors.toList()));

            return qDto;
        }).collect(Collectors.toList()));

        return dto;
    }

}

