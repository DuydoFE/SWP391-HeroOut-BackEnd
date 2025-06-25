package com.demo.demo.service;

import com.demo.demo.dto.ScheduleResponse;
import com.demo.demo.entity.Schedule;
import com.demo.demo.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    // ✅ Trả về danh sách ScheduleResponse cho API
    public List<ScheduleResponse> getAllSchedules() {
        return scheduleRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }


    // ✅ Trả về Optional
    public Optional<ScheduleResponse> getScheduleById(long id) {
        return scheduleRepository.findById(id)
                .map(this::toResponse);
    }

    // ✅ Convert entity → DTO
    private ScheduleResponse toResponse(Schedule schedule) {
        ScheduleResponse dto = new ScheduleResponse();
        dto.setId(schedule.getId());
        dto.setDate(schedule.getDate());
        dto.setRecurrence(schedule.getRecurrence());

        // Lấy slotId
        if (schedule.getSlot() != null) {
            dto.setSlotId(schedule.getSlot().getId());
        }

        // Lấy consultantId
        if (schedule.getConsultant() != null) {
            dto.setConsultantId(schedule.getConsultant().getId());
        }

        // Lấy appointmentId nếu có
        if (schedule.getAppointment() != null) {
            dto.setAppointmentId(schedule.getAppointment().getId());
        } else {
            dto.setAppointmentId(0);
        }

        return dto;
    }
    public List<ScheduleResponse> getSchedulesByConsultantId(long consultantId) {
        return scheduleRepository.findByConsultantId(consultantId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
