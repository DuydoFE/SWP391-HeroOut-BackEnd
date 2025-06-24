package com.demo.demo.service;

import com.demo.demo.dto.EventParticipationRequest;
import com.demo.demo.dto.EventParticipationResponse;
import com.demo.demo.entity.EventParticipation;
import com.demo.demo.enums.EventParticipationStatus;
import com.demo.demo.repository.AuthenticationRepository;
import com.demo.demo.repository.EventParticipationRepository;
import com.demo.demo.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventParticipationService {

    @Autowired
    private EventParticipationRepository eventParticipationRepository;
    @Autowired
    private AuthenticationRepository accountRepository;

    @Autowired
    private EventRepository eventRepository;


    @Autowired
    private ModelMapper modelMapper;

    public EventParticipationResponse toResponse(EventParticipation participation) {
        Long accountId = participation.getAccount() != null ? participation.getAccount().getId() : null;
        String accountName = participation.getAccount() != null ? participation.getAccount().getName() : null;
        String accountGmail = participation.getAccount() != null ? participation.getAccount().getEmail() : null;

        return new EventParticipationResponse(
                participation.getId(),
                accountId,
                accountName,
                accountGmail,
                participation.getEvent() != null ? participation.getEvent().getId() : null,
                participation.getCheckInTime(),
                participation.getCheckOutTime(),
                participation.getStatus()
        );
    }

    public List<EventParticipationResponse> getAllParticipationResponses() {
        return eventParticipationRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public EventParticipation getParticipationById(Long id) {
        return eventParticipationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Participation not found with ID: " + id));
    }

    public EventParticipation createParticipation(EventParticipationRequest request) {

        boolean alreadyExists = eventParticipationRepository
                .findByAccountIdAndEventId(request.getAccountId(), request.getEventId())
                .isPresent();

        if (alreadyExists) {
            throw new IllegalStateException("You have already registered for this event!");
        }

        EventParticipation participation = new EventParticipation();

        participation.setAccount(accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Account not found")));
        participation.setEvent(eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new EntityNotFoundException("Event not found")));

        participation.setStatus(EventParticipationStatus.REGISTERED);

        return eventParticipationRepository.save(participation);
    }


    public EventParticipation updateParticipation(Long id, EventParticipationRequest request) {
        EventParticipation existing = eventParticipationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Participation not found"));

        existing.setAccount(accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Account not found")));
        existing.setEvent(eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new EntityNotFoundException("Event not found")));

        existing.setCheckInTime(request.getCheckInTime());
        existing.setCheckOutTime(request.getCheckOutTime());
        existing.setStatus(request.getStatus());

        return eventParticipationRepository.save(existing);
    }


    public void deleteParticipation(Long id) {
        if (!eventParticipationRepository.existsById(id)) {
            throw new EntityNotFoundException("Participation not found with ID: " + id);
        }
        eventParticipationRepository.deleteById(id);
    }

    public EventParticipation checkInParticipation(Long id) {
        EventParticipation existing = eventParticipationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Participation not found"));

        existing.setCheckInTime(LocalDateTime.now());
        existing.setStatus(EventParticipationStatus.CHECKED_IN);
        return eventParticipationRepository.save(existing);
    }

    public EventParticipation checkOutParticipation(Long id) {
        EventParticipation existing = eventParticipationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Participation not found"));

        existing.setCheckOutTime(LocalDateTime.now());
        existing.setStatus(EventParticipationStatus.CHECKED_OUT);
        return eventParticipationRepository.save(existing);
    }

    public List<EventParticipation> getAllEventParticipationByAccountId(Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new EntityNotFoundException("Account not found");
        }

        return eventParticipationRepository.findAllByAccountId(accountId);
    }

    public List<EventParticipationResponse> getAllByStatus(EventParticipationStatus status) {
        return eventParticipationRepository.findAllByStatus(status)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

}
