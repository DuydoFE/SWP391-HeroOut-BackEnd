package com.demo.demo.service;

import com.demo.demo.dto.AppointmentRequest;
import com.demo.demo.dto.AppointmentResponse;
import com.demo.demo.dto.CheckInResponse;
import com.demo.demo.entity.Account;
import com.demo.demo.entity.Appointment;
import com.demo.demo.entity.Consultant;
import com.demo.demo.entity.Schedule;
import com.demo.demo.enums.AppointmentStatus;
import com.demo.demo.enums.Role;
import com.demo.demo.exception.exceptions.BadRequestException;
import com.demo.demo.exception.exceptions.ResourceNotFoundException;
import com.demo.demo.repository.AppointmentRepository;
import com.demo.demo.repository.AuthenticationRepository;
import com.demo.demo.repository.ConsultantRepository;
import com.demo.demo.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Arrays;

@Service
public class AppointmentService {
    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired(required = false)
    AuthenticationRepository authenticationRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    private JitsiService jitsiService;

    @Autowired
    private EmailService emailService;

    @Autowired
    ConsultantRepository consultantRepository;


    @Transactional
    public Appointment create(AppointmentRequest appointmentRequest) {

        Schedule slot = scheduleRepository.findById(appointmentRequest.getScheduleId())
                .orElseThrow(() -> new ResourceNotFoundException("Schedule slot not found with id " + appointmentRequest.getScheduleId()));

        if (slot.getConsultant() == null || slot.getConsultant().getAccount() == null) {
            throw new IllegalStateException("Schedule with ID " + appointmentRequest.getScheduleId() + " is not properly associated with a consultant account.");
        }

        if (!slot.isBooked()) {
            throw new BadRequestException("Schedule slot is already booked or unavailable");
        }

        Account currentAccount = authenticationService.getCurrentAccount();

        Appointment appointment = new Appointment();
        appointment.setCreateAt(LocalDate.now());
        appointment.setStatus(AppointmentStatus.BOOKED);
        appointment.setAccount(currentAccount);
        appointment.setSchedule(slot);
        appointment.setDescription(appointmentRequest.getDescription());

        Appointment savedAppointment = appointmentRepository.save(appointment);

        slot.setBooked(false);
        scheduleRepository.save(slot);

        return savedAppointment;
    }



    public CheckInResponse checkInAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (!appointment.isCheckedIn()) {
            Account member = appointment.getAccount();
            Schedule schedule = appointment.getSchedule();
            Consultant consultant = schedule.getConsultant();

            if (consultant == null || consultant.getAccount() == null) {
                throw new IllegalStateException("Consultant account not found for this schedule associated with the appointment");
            }

            Account consultantAccount = consultant.getAccount();

            String meetingLink = jitsiService.createMeetingRoom("appointment-" + appointmentId);

            try {
                emailService.sendMeetingLink(member.getEmail(), member.getName(), meetingLink);
                if (consultantAccount.getEmail() != null && !consultantAccount.getEmail().isEmpty()) {
                    emailService.sendMeetingLink(consultantAccount.getEmail(), consultantAccount.getName(), meetingLink);
                } else {
                    // Log warning instead of throwing exception if email is missing but account exists
                    System.err.println("Consultant email is missing for appointment ID: " + appointmentId + ", Consultant Account ID: " + consultantAccount.getId());
                }
            } catch (Exception e) {
                System.err.println("Failed to send meeting link email for appointment ID " + appointmentId + ": " + e.getMessage());
                // Depending on requirements, you might rethrow or handle differently
            }

            appointment.setMeetingLink(meetingLink);
            appointment.setCheckedIn(true);

            appointmentRepository.save(appointment);

            CheckInResponse response = new CheckInResponse();
            response.setAppointmentId(appointment.getId());
            response.setMeetingLink(appointment.getMeetingLink());
            response.setCheckedIn(true);
            return response;

        } else {
            CheckInResponse response = new CheckInResponse();
            response.setAppointmentId(appointment.getId());
            response.setMeetingLink(appointment.getMeetingLink());
            response.setCheckedIn(true);
            return response;
        }
    }


    public AppointmentResponse getAppointmentById(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id " + appointmentId));
        return mapToAppointmentResponse(appointment);
    }


    public List<AppointmentResponse> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .map(this::mapToAppointmentResponse)
                .collect(Collectors.toList());
    }


    public List<AppointmentResponse> getAppointmentsByAccountId(Long accountId) {
        List<Appointment> appointments = appointmentRepository.findByAccountId(accountId);
        return appointments.stream()
                .map(this::mapToAppointmentResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public void updateAppointmentStatus(Long appointmentId, AppointmentStatus newStatus) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id " + appointmentId));

        appointment.setStatus(newStatus);
        appointmentRepository.save(appointment);

        if (newStatus == AppointmentStatus.CANCELLED) {
            Schedule schedule = appointment.getSchedule();
            if (schedule != null) {
                schedule.setBooked(true);
                scheduleRepository.save(schedule);
            }
        }
    }

    // --- START MODIFICATION ---
    private AppointmentResponse mapToAppointmentResponse(Appointment appointment) {
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setCreateAt(appointment.getCreateAt());
        response.setDescription(appointment.getDescription());
        response.setStatus(appointment.getStatus());
        response.setMeetingLink(appointment.getMeetingLink());
        response.setCheckedIn(appointment.isCheckedIn());

        // Map Account details (client)
        if (appointment.getAccount() != null) {
            response.setAccountId(appointment.getAccount().getId());
            response.setAccountName(appointment.getAccount().getName());
        } else {
            response.setAccountId(null);
            response.setAccountName(null); // Set name to null if account is null
        }

        // Map Schedule details
        if (appointment.getSchedule() != null) {
            response.setScheduleId(appointment.getSchedule().getId());
            response.setAppointmentDate(appointment.getSchedule().getDate());

            // Map Consultant details from Schedule
            if (appointment.getSchedule().getConsultant() != null) {
                Consultant consultant = appointment.getSchedule().getConsultant();
                response.setConsultantId(consultant.getId()); // Set consultant ID from Consultant entity

                // Get consultant name from the linked Account
                if (consultant.getAccount() != null) {
                    response.setConsultantName(consultant.getAccount().getName()); // Set consultant name
                } else {
                    response.setConsultantName(null); // Set name to null if consultant's account is null
                }

            } else {
                response.setConsultantId(null);
                response.setConsultantName(null); // Set name to null if consultant is null
            }

        } else {
            // Handle case where schedule is null
            response.setConsultantId(null);
            response.setConsultantName(null);
            response.setScheduleId(null);
            response.setAppointmentDate(null);
        }

        return response;
    }

}