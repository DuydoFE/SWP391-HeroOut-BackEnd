package com.demo.demo.service;

import com.demo.demo.dto.EventRequest;
import com.demo.demo.entity.Event;
import com.demo.demo.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        public Event createNewEvent(EventRequest eventRequest) {
            Event event = modelMapper.map(eventRequest, Event.class);
            return eventRepository.save(event);
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
