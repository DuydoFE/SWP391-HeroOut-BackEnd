package com.demo.demo.service;

import com.demo.demo.entity.EventParticipation;
import com.demo.demo.repository.EventParticipationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventParticipationService {

    @Autowired
    EventParticipationRepository eventParticipationRepository;

    public EventParticipation getEventParticipationByID(long id) {
        return eventParticipationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("EventParticipation not found with id: " + id));
    }

    public List<EventParticipation> getAllEventParticipations() {
        return eventParticipationRepository.findAll();
    }

    public EventParticipation saveEventParticipation(EventParticipation participation) {
        return eventParticipationRepository.save(participation);
    }

    public EventParticipation updateEventParticipation(EventParticipation participation) {
        return eventParticipationRepository.save(participation);
    }

    public void deleteEventParticipation(long id) {
        if (!eventParticipationRepository.existsById(id)) {
            throw new EntityNotFoundException("Cannot delete. ID not found: " + id);
        }
        eventParticipationRepository.deleteById(id);
    }

}
