package com.demo.demo.repository;

import com.demo.demo.entity.EventParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventParticipationRepository extends JpaRepository<EventParticipation, Long> {
}
