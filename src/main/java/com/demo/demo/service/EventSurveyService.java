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

    public EventSurveyDTO createSurvey(EventSurveyDTO dto) {
        Event event = eventRepo.findById(dto.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        EventSurvey survey = new EventSurvey();
        survey.setTitle(dto.getTitle());
        survey.setEvent(event);

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
        return mapSurveyToDTO(surveyRepo.save(survey));
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

        // Map câu hỏi cũ theo id để tiện đối chiếu
        Map<Long, EventSurveyQuestion> oldQuestions = survey.getQuestions().stream()
                .collect(Collectors.toMap(EventSurveyQuestion::getId, q -> q));

        List<EventSurveyQuestion> updatedQuestions = new ArrayList<>();

        for (EventSurveyQuestionDTO qDto : dto.getQuestions()) {
            EventSurveyQuestion question;

            // Nếu có id thì là update
            if (qDto.getId() != null && oldQuestions.containsKey(qDto.getId())) {
                question = oldQuestions.remove(qDto.getId()); // lấy ra và đánh dấu là đã xử lý
                question.setQuestionText(qDto.getQuestionText());

                // Xóa hết options cũ (có thể viết thông minh hơn nếu cần)
                question.getOptions().clear();

            } else {
                // Câu hỏi mới
                question = new EventSurveyQuestion();
                question.setEventSurvey(survey);
                question.setQuestionText(qDto.getQuestionText());
            }

            // Gán options mới
            List<EventSurveyOption> options = qDto.getOptions().stream().map(oDto -> {
                EventSurveyOption o = new EventSurveyOption();
                o.setContent(oDto.getContent());
                o.setScore(oDto.getScore());
                o.setQuestion(question);
                return o;
            }).collect(Collectors.toList());

            question.setOptions(options);
            updatedQuestions.add(question);
        }

        // Xóa các câu hỏi đã bị xoá (không còn trong dto)
        for (EventSurveyQuestion toDelete : oldQuestions.values()) {
            questionRepo.delete(toDelete);
        }

        survey.setQuestions(updatedQuestions);

        return mapSurveyToDTO(surveyRepo.save(survey));
    }


    public void deleteSurvey(Long eventId) {
        surveyRepo.deleteByEventId(eventId);
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

