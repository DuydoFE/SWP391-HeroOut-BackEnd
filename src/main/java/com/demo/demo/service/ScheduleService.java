package com.demo.demo.service;

import com.demo.demo.dto.ScheduleRequest;
import com.demo.demo.dto.ScheduleResponse;
import com.demo.demo.entity.Consultant;
import com.demo.demo.entity.Schedule;
import com.demo.demo.entity.Slot;
import com.demo.demo.exception.exceptions.BadRequestException;
import com.demo.demo.repository.ConsultantRepository;
import com.demo.demo.repository.ScheduleRepository;
import com.demo.demo.repository.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final SlotRepository slotRepository;
    private final ConsultantRepository consultantRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository,
                           SlotRepository slotRepository,
                           ConsultantRepository consultantRepository) {
        this.scheduleRepository = scheduleRepository;
        this.slotRepository = slotRepository;
        this.consultantRepository = consultantRepository;
    }

    // Lấy toàn bộ Schedule
    public List<ScheduleResponse> getAllSchedules() {
        return scheduleRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Lấy Schedule theo ID
    public Optional<ScheduleResponse> getScheduleById(long id) {
        return scheduleRepository.findById(id)
                .map(this::toResponse);
    }

    // Lấy theo consultantId
    public List<ScheduleResponse> getSchedulesByConsultantId(long consultantId) {
        return scheduleRepository.findByConsultantId(consultantId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ✅ Tạo Schedule mới
    public ScheduleResponse createSchedule(ScheduleRequest request) {
        Slot slot = slotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new BadRequestException("Slot không tồn tại"));

        Consultant consultant = consultantRepository.findById(request.getConsultantId())
                .orElseThrow(() -> new BadRequestException("Consultant không tồn tại"));

        Schedule schedule = new Schedule();
        schedule.setDate(request.getDate());
        schedule.setRecurrence(request.getRecurrence());
        schedule.setSlot(slot);
        schedule.setConsultant(consultant);
        schedule.setBooked(false); // Mặc định chưa đặt

        Schedule saved = scheduleRepository.save(schedule);
        return toResponse(saved);
    }

    // ✅ Cập nhật Schedule
    public ScheduleResponse updateSchedule(Long id, ScheduleRequest request) {
        Schedule existing = scheduleRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy lịch hẹn"));

        if (request.getDate() != null) {
            existing.setDate(request.getDate());
        }

        if (request.getRecurrence() != null) {
            existing.setRecurrence(request.getRecurrence());
        }

        if (request.getSlotId() != null) {
            Slot slot = slotRepository.findById(request.getSlotId())
                    .orElseThrow(() -> new BadRequestException("Slot không tồn tại"));
            existing.setSlot(slot);
        }

        if (request.getConsultantId() != null) {
            Consultant consultant = consultantRepository.findById(request.getConsultantId())
                    .orElseThrow(() -> new BadRequestException("Consultant không tồn tại"));
            existing.setConsultant(consultant);
        }

        Schedule saved = scheduleRepository.save(existing);
        return toResponse(saved);
    }

    // ✅ Xoá Schedule nếu chưa bị đặt
    public void deleteSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy lịch hẹn"));

        if (schedule.isBooked()) {
            throw new BadRequestException("Không thể xoá lịch đã được đặt");
        }

        scheduleRepository.delete(schedule);
    }

    // Convert entity -> DTO
    private ScheduleResponse toResponse(Schedule schedule) {
        ScheduleResponse dto = new ScheduleResponse();
        dto.setId(schedule.getId());
        dto.setDate(schedule.getDate());
        dto.setRecurrence(schedule.getRecurrence());

        if (schedule.getSlot() != null) {
            dto.setSlotId(schedule.getSlot().getId());
        }

        if (schedule.getConsultant() != null) {
            dto.setConsultantId(schedule.getConsultant().getId());
        }

        dto.setAppointmentId(
                schedule.getAppointment() != null ? schedule.getAppointment().getId() : null
        );

        return dto;
    }
}
