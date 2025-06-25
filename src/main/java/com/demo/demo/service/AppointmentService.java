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

@Service
public class AppointmentService {
    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    AuthenticationRepository authenticationRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    private JitsiService jitsiService;

    @Autowired
    private EmailService emailService;


    @Transactional
    public Appointment create(AppointmentRequest appointmentRequest) {

        // Tìm tài khoản của consultant
        Account consultant = authenticationRepository.findById(appointmentRequest.getAccountId())
                .orElseThrow(() -> new RuntimeException("Consultant not found"));

        // Kiểm tra role của consultant
        if (!consultant.getRole().equals(Role.CONSULTANT)) {
            throw new BadRequestException("Account is not a consultant");
        }

        // Tìm schedule theo slot, consultant, và ngày
        Schedule schedule = scheduleRepository.findBySlotIdAndConsultantIdAndDate(
                appointmentRequest.getSlotId(),
                consultant.getConsultant().getId(),  // Lấy từ Account → Consultant
                appointmentRequest.getAppointmentDate()
        ).orElseThrow(() -> new BadRequestException("Schedule not found"));

        // Kiểm tra xem slot đã được đặt chưa
        if (schedule.isBooked()) {
            throw new BadRequestException("Slot is already booked");
        }

        // Lấy tài khoản hiện tại (người đặt lịch)
        Account currentAccount = authenticationService.getCurrentAccount();

        // Tạo Appointment mới
        Appointment appointment = new Appointment();
        appointment.setCreateAt(LocalDate.now());
        appointment.setStatus(AppointmentStatus.BOOKED);
        appointment.setAccount(currentAccount);
        appointment.setSchedule(schedule);  // Gán lịch hẹn

        // Gán appointment vào schedule
        schedule.setAppointment(appointment);
        schedule.setBooked(true); // Đánh dấu là đã được đặt

        // Lưu cả appointment và schedule
        appointmentRepository.save(appointment);
        scheduleRepository.save(schedule);

        return appointment;
    }

    public String checkInAppointment(Long appointmentId) {
        Optional<Appointment> optional = appointmentRepository.findById(appointmentId);
        if (optional.isPresent()) {
            Appointment appointment = optional.get();
            Account account = appointment.getAccount();

            String meetingLink = jitsiService.createMeetingRoom(account.getName());
            emailService.sendMeetingLink(account.getEmail(), account.getName(), meetingLink);
            return meetingLink;
        } else {
            throw new RuntimeException("Appointment not found");
        }
    }

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
