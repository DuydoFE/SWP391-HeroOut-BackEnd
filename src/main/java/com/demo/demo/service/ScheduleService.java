// src/main/java/com/demo/demo/service/ScheduleService.java
package com.demo.demo.service;

import com.demo.demo.entity.Schedule;
import com.demo.demo.entity.Slot; // Cần import Slot entity
import com.demo.demo.repository.ScheduleRepository;
import com.demo.demo.dto.ScheduleResponseDto; // Import DTO
import com.demo.demo.dto.SlotDto; // Import DTO

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors; // Cần cho stream().map().collect()
import java.time.LocalTime; // Cần import LocalTime


@Service // Marks this class as a Spring Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    // Constructor injection for the repository
    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    // Helper method to map Schedule entity to ScheduleResponseDto
    private ScheduleResponseDto mapToDto(Schedule schedule) {
        if (schedule == null) {
            return null;
        }

        SlotDto slotDto = null;
        Slot slot = schedule.getSlot(); // Lấy Slot entity từ Schedule
        if (slot != null) {
            // Tạo SlotDto từ Slot entity
            // Lấy dữ liệu LocalTime từ getSlot_start() và getSlot_end()
            slotDto = new SlotDto(slot.getSlot_start(), slot.getSlot_end());
        }

        // Tạo ScheduleResponseDto từ Schedule entity và SlotDto
        return new ScheduleResponseDto(
                schedule.getId(),
                schedule.getDate(),
                schedule.getRecurrence(),
                slotDto // Gán SlotDto đã tạo
        );
    }


    // Method to get all schedules, returns DTOs
    public List<ScheduleResponseDto> getAllSchedules() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return schedules.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Method to get schedules by consultant ID, returns DTOs
    public List<ScheduleResponseDto> getSchedulesByConsultantId(long consultantId ) {
        List<Schedule> schedules = scheduleRepository.findByConsultantId(consultantId);
        return schedules.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Optional: Method to get a single schedule by its own ID, returns DTO
    public ScheduleResponseDto getScheduleById(long id) {
        return scheduleRepository.findById(id)
                .map(this::mapToDto)
                .orElse(null);
    }
}