package com.demo.demo.repository;

import com.demo.demo.entity.Account;

import com.demo.demo.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByConsultantId(long consultantId);
    Optional<Schedule> findBySlotIdAndConsultantIdAndDate(Long slotId, Long consultantId, LocalDate date);
    List<Schedule> findByConsultantIdAndDateAndSlotIdInAndIsBookedFalse(
            long consultantId,
            LocalDate date,
            List<Long> slotIds
    );

}
