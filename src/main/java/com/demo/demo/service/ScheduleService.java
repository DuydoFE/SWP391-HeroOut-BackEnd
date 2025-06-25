package com.demo.demo.service;

import com.demo.demo.entity.Schedule;
import com.demo.demo.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Marks this class as a Spring Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    // Constructor injection for the repository
    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    // Method to get all schedules
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll(); // Uses the default findAll from JpaRepository
    }

    // Method to get schedules by account ID
    public List<Schedule> getSchedulesByAccountId(long consultantId ) {
        return scheduleRepository.findByConsultantId(consultantId); // Uses the custom method defined in the repository
    }

    // Optional: Method to get a single schedule by its own ID
    public Schedule getScheduleById(long id) {
        return scheduleRepository.findById(id).orElse(null); // Returns null if not found
    }
}