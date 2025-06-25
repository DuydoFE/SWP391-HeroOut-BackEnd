package com.demo.demo.repository;

import com.demo.demo.entity.Appointment;
import com.demo.demo.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {


}