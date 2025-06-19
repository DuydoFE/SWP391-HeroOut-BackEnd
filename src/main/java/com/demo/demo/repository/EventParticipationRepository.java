package com.demo.demo.repository;

import com.demo.demo.entity.EventParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventParticipationRepository extends JpaRepository<EventParticipation, Long> {
    Optional<EventParticipation> findByAccountIdAndEventId(Long accountId, Long eventId);
}
