package com.demo.demo.repository;

import com.demo.demo.entity.Account;

import com.demo.demo.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findScheduleByAccountAndDate(Account account, LocalDate date);

    List<Schedule> findByAccountId(long accountId);
    Schedule findScheduleBySlotIdAndAccountAndDate(Long slotId, Account account, LocalDate date);
}
