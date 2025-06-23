package com.demo.demo.service;

import com.demo.demo.dto.EventRequest;
import com.demo.demo.entity.Event;
import com.demo.demo.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

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

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + id));
    }

    public void removeEvent(Event event) {
        eventRepository.delete(event);
    }

    public Event updateEvent(Long id, Event updatedEvent) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + id));

        // Cập nhật các trường (bạn cần chỉnh theo entity thật của bạn)
        existingEvent.setTitle(updatedEvent.getTitle());
        existingEvent.setLocation(updatedEvent.getLocation());
        existingEvent.setStartTime(updatedEvent.getStartTime());
        existingEvent.setEndTime(updatedEvent.getEndTime());
        existingEvent.setDescription(updatedEvent.getDescription());

        return eventRepository.save(existingEvent);
    }

    public void removeEventById(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new EntityNotFoundException("Event not found with ID: " + id);
        }
        eventRepository.deleteById(id);
    }
}
