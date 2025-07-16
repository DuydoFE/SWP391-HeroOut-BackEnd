package com.demo.demo.repository;

import com.demo.demo.entity.Account;
import com.demo.demo.entity.Event;
import com.demo.demo.entity.EventParticipation;
import com.demo.demo.enums.EventParticipationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventParticipationRepository extends JpaRepository<EventParticipation, Long> {
    Optional<EventParticipation> findByAccountIdAndEventId(Long accountId, Long eventId);
    List<EventParticipation> findAllByAccountId(Long accountId);
    List<EventParticipation> findAllByEventIdAndStatus(Long eventId, EventParticipationStatus status);
    Optional<EventParticipation> findByEventAndAccount(Event event, Account account);

}
