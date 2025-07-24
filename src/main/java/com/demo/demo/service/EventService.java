package com.demo.demo.service;

import com.demo.demo.dto.EventDTO;
import com.demo.demo.dto.EventRequest;
import com.demo.demo.entity.Event;
import com.demo.demo.entity.EventSurvey;
import com.demo.demo.repository.EventRepository;
import com.demo.demo.repository.EventSurveyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventSurveyRepository eventSurveyRepository;

    @Autowired
    private ModelMapper modelMapper;



    public Event createEvent(EventRequest request) {
        validateEventTime(request.getStartTime(), request.getEndTime());

        Event event = modelMapper.map(request, Event.class);
        return eventRepository.save(event);
    }
    private void validateEventTime(LocalDateTime startTime, LocalDateTime endTime) {
        LocalDateTime now = LocalDateTime.now();

        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("startTime và endTime không được null");
        }

        if (startTime.isBefore(now)) {
            throw new IllegalArgumentException("startTime phải lớn hơn hoặc bằng thời điểm hiện tại");
        }

        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("startTime phải nhỏ hơn endTime");
        }
    }
    public List<EventDTO> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        List<EventDTO> result = new ArrayList<>();

        for (Event event : events) {
            EventDTO dto = new EventDTO();
            dto.setId(event.getId());
            dto.setTitle(event.getTitle());
            dto.setDescription(event.getDescription());
            dto.setLocation(event.getLocation());
            dto.setStartTime(event.getStartTime());
            dto.setEndTime(event.getEndTime());
            result.add(dto);
        }

        return result;
    }

    public EventDTO getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + id));

        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setLocation(event.getLocation());
        dto.setStartTime(event.getStartTime());
        dto.setEndTime(event.getEndTime());

        return dto;
    }


    public void removeEvent(Event event) {
        eventRepository.delete(event);
    }

    public Event updateEvent(Long id, EventRequest request) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + id));

        validateEventTime(request.getStartTime(), request.getEndTime());

        existingEvent.setTitle(request.getTitle());
        existingEvent.setLocation(request.getLocation());
        existingEvent.setStartTime(request.getStartTime());
        existingEvent.setEndTime(request.getEndTime());
        existingEvent.setDescription(request.getDescription());

        return eventRepository.save(existingEvent);
    }


    @Transactional
    public void removeEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + id));

        EventSurvey survey = event.getEventSurvey();
        if (survey != null) {
            survey.setEvent(null);
            event.setEventSurvey(null);
            eventSurveyRepository.delete(survey);
        }

        // Cuối cùng xoá Event
        eventRepository.delete(event);
    }

}
