package com.demo.demo.service;

import com.demo.demo.dto.EventSurveyOptionDTO;
import com.demo.demo.entity.EventSurveyOption;
import com.demo.demo.entity.EventSurveyQuestion;
import com.demo.demo.repository.EventSurveyOptionRepository;
import com.demo.demo.repository.EventSurveyQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventSurveyOptionService {

    private final EventSurveyOptionRepository optionRepo;
    private final EventSurveyQuestionRepository questionRepo;

    public EventSurveyOptionDTO createOption(EventSurveyOptionDTO dto) {
        EventSurveyQuestion question = questionRepo.findById(dto.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found"));

        EventSurveyOption option = new EventSurveyOption();
        option.setContent(dto.getContent());
        option.setScore(dto.getScore());
        option.setQuestion(question);

        return mapToDTO(optionRepo.save(option));
    }

    public EventSurveyOptionDTO updateOption(Long id, EventSurveyOptionDTO dto) {
        EventSurveyOption option = optionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Option not found"));

        option.setContent(dto.getContent());
        option.setScore(dto.getScore());

        return mapToDTO(optionRepo.save(option));
    }

    public void deleteOption(Long id) {
        optionRepo.deleteById(id);
    }

    public EventSurveyOptionDTO getOption(Long id) {
        return optionRepo.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Option not found"));
    }

    public List<EventSurveyOptionDTO> getOptionsByQuestion(Long questionId) {
        List<EventSurveyOption> options = optionRepo.findByQuestion_Id(questionId);
        return options.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private EventSurveyOptionDTO mapToDTO(EventSurveyOption opt) {
        EventSurveyOptionDTO dto = new EventSurveyOptionDTO();
        dto.setId(opt.getId());
        dto.setContent(opt.getContent());
        dto.setScore(opt.getScore());
        dto.setQuestionId(opt.getQuestion().getId());
        return dto;
    }
}
