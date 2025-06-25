package com.demo.demo.service;

import com.demo.demo.dto.AppointmentRequest;
import com.demo.demo.dto.AppointmentResponse;
import com.demo.demo.entity.Account;

import com.demo.demo.entity.Appointment;

import com.demo.demo.entity.Schedule;

import com.demo.demo.enums.AppointmentStatus;
import com.demo.demo.enums.Role;
import com.demo.demo.exception.exceptions.BadRequestException;
import com.demo.demo.repository.AppointmentRepository;
import com.demo.demo.repository.AuthenticationRepository;

import com.demo.demo.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JitsiService jitsiService;

    @Autowired
    private EmailService emailService;

    // 🔵 Tạo Appointment (đã có)
    @Transactional
    public AppointmentResponse create(AppointmentRequest request) {
        Account consultantAccount = authenticationRepository.findById(request.getAccountId())
                .orElseThrow(() -> new BadRequestException("Consultant not found"));

        if (consultantAccount.getRole() != Role.CONSULTANT) {
            throw new BadRequestException("Account is not a consultant");
        }

        Long consultantId = consultantAccount.getConsultant().getId();
        Schedule schedule = scheduleRepository.findBySlotIdAndConsultantIdAndDate(
                request.getSlotId(), consultantId, request.getAppointmentDate()
        ).orElseThrow(() -> new BadRequestException("Schedule not found"));

        if (schedule.isBooked()) {
            throw new BadRequestException("Schedule is already booked");
        }

        Account member = authenticationService.getCurrentAccount();

        Appointment appointment = new Appointment();
        appointment.setCreateAt(LocalDate.now());
        appointment.setStatus(AppointmentStatus.BOOKED);
        appointment.setAccount(member);
        appointment.setSchedule(schedule);

        schedule.setAppointment(appointment);
        schedule.setBooked(true);

        appointmentRepository.save(appointment);
        scheduleRepository.save(schedule);

        return toResponse(appointment);
    }

    // 🔵 Đọc tất cả
    public List<AppointmentResponse> getAll() {
        return appointmentRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // 🔵 Đọc theo ID
    public AppointmentResponse getById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Appointment not found"));
        return toResponse(appointment);
    }

    // 🟡 Cập nhật trạng thái (hoặc mở rộng nếu cần)
    public AppointmentResponse updateStatus(Long id, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Appointment not found"));

        appointment.setStatus(status);
        Appointment saved = appointmentRepository.save(appointment);
        return toResponse(saved);
    }

    // 🔴 Xoá appointment (và mở slot)
    @Transactional
    public void delete(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Appointment not found"));

        Schedule schedule = appointment.getSchedule();

        // Xoá liên kết giữa schedule và appointment
        schedule.setAppointment(null);
        schedule.setBooked(false);

        scheduleRepository.save(schedule);
        appointmentRepository.delete(appointment);
    }

    // 🔵 Check-in (gửi link Jitsi) - đã có
    public String checkInAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new BadRequestException("Appointment not found"));

        Account account = appointment.getAccount();
        String link = jitsiService.createMeetingRoom(account.getName());
        emailService.sendMeetingLink(account.getEmail(), account.getName(), link);
        return link;
    }

    // Mapping DTO
    public AppointmentResponse toResponse(Appointment appointment) {
        AppointmentResponse dto = new AppointmentResponse();
        dto.setId(appointment.getId());
        dto.setCreateAt(appointment.getCreateAt());
        dto.setStatus(appointment.getStatus());
        dto.setAccountId(appointment.getAccount().getId());
        dto.setScheduleId(appointment.getSchedule().getId());
        return dto;
    }
}


