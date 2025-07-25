package com.demo.demo.service;

import com.demo.demo.dto.EventSurveyOptionDTO;
import com.demo.demo.dto.EventSurveyQuestionDTO;
import com.demo.demo.entity.EventSurvey;
import com.demo.demo.entity.EventSurveyOption;
import com.demo.demo.entity.EventSurveyQuestion;
import com.demo.demo.repository.EventSurveyOptionRepository;
import com.demo.demo.repository.EventSurveyQuestionRepository;
import com.demo.demo.repository.EventSurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventSurveyQuestionService {

    private final EventSurveyQuestionRepository questionRepo;
    private final EventSurveyRepository surveyRepo;
    private final EventSurveyOptionRepository optionRepo;
    private final EventSurveyOptionService optionService;

    public EventSurveyQuestionDTO createQuestion(EventSurveyQuestionDTO dto, Long surveyId) {
        EventSurvey survey = surveyRepo.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        EventSurveyQuestion question = new EventSurveyQuestion();
        question.setQuestionText(dto.getQuestionText());
        question.setEventSurvey(survey);

        List<EventSurveyOption> options = new ArrayList<>();
        if (dto.getOptions() != null) {
            for (EventSurveyOptionDTO oDto : dto.getOptions()) {
                EventSurveyOption option = new EventSurveyOption();
                option.setContent(oDto.getContent());
                option.setScore(oDto.getScore());
                option.setQuestion(question);
                options.add(option);
            }
        }
        question.setOptions(options);

        return mapToDTO(questionRepo.save(question));
    }

    @Transactional
    public EventSurveyQuestionDTO updateQuestion(Long id, EventSurveyQuestionDTO dto) {
        EventSurveyQuestion question = questionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        question.setQuestionText(dto.getQuestionText());

        // Cập nhật hoặc thêm mới option
        List<Long> incomingIds = new ArrayList<>();
        for (EventSurveyOptionDTO oDto : dto.getOptions()) {
            if (oDto.getId() != null) {
                optionService.updateOption(oDto.getId(), oDto);
                incomingIds.add(oDto.getId());
            } else {
                // Tạo mới option
                EventSurveyOption newOption = new EventSurveyOption();
                newOption.setContent(oDto.getContent());
                newOption.setScore(oDto.getScore());
                newOption.setQuestion(question);
                question.getOptions().add(newOption);
            }
        }

        // Xóa option cũ không còn trong danh sách mới (để orphanRemoval hoạt động đúng)
        question.getOptions().removeIf(option -> option.getId() != null && !incomingIds.contains(option.getId()));

        return mapToDTO(questionRepo.save(question));
    }


    public void deleteQuestion(Long id) {
        questionRepo.deleteById(id);
    }

    public EventSurveyQuestionDTO getQuestion(Long id) {
        return questionRepo.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Question not found"));
    }

    public List<EventSurveyQuestionDTO> getQuestionsBySurvey(Long surveyId) {
        return questionRepo.findByEventSurveyId(surveyId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private EventSurveyQuestionDTO mapToDTO(EventSurveyQuestion question) {
        EventSurveyQuestionDTO dto = new EventSurveyQuestionDTO();
        dto.setId(question.getId());
        dto.setQuestionText(question.getQuestionText());

        List<EventSurveyOptionDTO> optionDTOs = new ArrayList<>();
        if (question.getOptions() != null) {
            for (EventSurveyOption option : question.getOptions()) {
                EventSurveyOptionDTO oDto = new EventSurveyOptionDTO();
                oDto.setId(option.getId());
                oDto.setContent(option.getContent());
                oDto.setScore(option.getScore());
                oDto.setQuestionId(question.getId());
                optionDTOs.add(oDto);
            }
        }
        dto.setOptions(optionDTOs);

        return dto;
    }
}
