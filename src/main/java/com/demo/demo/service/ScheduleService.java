package com.demo.demo.service;

import com.demo.demo.entity.Consultant; // Import Consultant entity
import com.demo.demo.entity.Schedule;
import com.demo.demo.entity.Slot;
import com.demo.demo.repository.ScheduleRepository;
import com.demo.demo.dto.ScheduleResponseDto;
import com.demo.demo.dto.SlotDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalTime;


@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

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
        Long currentSlotId = null;
        Slot slot = schedule.getSlot();
        if (slot != null) {
            slotDto = new SlotDto(slot.getSlot_start(), slot.getSlot_end());
            currentSlotId = slot.getId();
        }

        // Chuyển đổi giá trị boolean isBooked sang int 0 hoặc 1
        int bookedStatusInt = schedule.isBooked() ? 1 : 0;


        Long currentConsultantId = null;
        Consultant consultant = schedule.getConsultant();
        if (consultant != null) {
            currentConsultantId = consultant.getId();
        }



        return new ScheduleResponseDto(
                schedule.getId(),
                schedule.getDate(),
                schedule.getRecurrence(),
                bookedStatusInt,
                currentSlotId,
                slotDto,
                currentConsultantId
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


    public ScheduleResponseDto getScheduleById(long id) {
        return scheduleRepository.findById(id)
                .map(this::mapToDto)
                .orElse(null);
    }
}