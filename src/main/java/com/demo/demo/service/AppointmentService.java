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
import java.util.stream.Collectors; // Import Collectors

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

        //find doctor
        Consultant consultant = consultantRepository.findById(appointmentRequest.getConsultantId()).orElseThrow(()->new ResourceNotFoundException("Consultant not found"));

        //check doctor
        if (!consultant.getAccount().getRole().equals(Role.CONSULTANT)){
            throw new BadRequestException("Account is not a consultant");
        }

        //tim slot
        Schedule slot = scheduleRepository.findScheduleBySlotIdAndConsultantAndDate(
                appointmentRequest.getSlotId(),
                consultant,
                appointmentRequest.getAppointmentDate()
        );
        if (slot == null) {
            throw new ResourceNotFoundException("Schedule slot not found for the given date and consultant");
        }

        //check xem slot da dat hay chua
        if(!slot.isBooked()){
            throw new BadRequestException("Slot is not available");
        }

        //lay tai khoan
        Account currentAccount = authenticationService.getCurrentAccount();


        //APPONTMENT
        Appointment appointment = new Appointment();
        appointment.setCreateAt(LocalDate.now());
        appointment.setStatus(AppointmentStatus.BOOKED);
        appointment.setAccount(currentAccount);
        appointment.setSchedule(slot);
        appointment.setDescription(appointmentRequest.getDescription());
        Appointment savedAppointment = appointmentRepository.save(appointment);

        //set slot do thanh da dat
        slot.setBooked(false); // Logic này vẫn cần kiểm tra lại ý nghĩa của `isBooked` true/false
        scheduleRepository.save(slot);

        return savedAppointment;
    }

    public CheckInResponse checkInAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        // Optional: Check if the current authenticated user is either the member or the consultant of the appointment
        // This depends on your security requirements. For now, let's assume any authenticated user can trigger check-in (less secure)
        // Or you might restrict this based on roles or ownership.
        // Account currentUser = authenticationService.getCurrentAccount();
        // if (!appointment.getAccount().getId().equals(currentUser.getId()) &&
        //     (appointment.getSchedule().getConsultant() == null ||
        //      !appointment.getSchedule().getConsultant().getAccount().getId().equals(currentUser.getId()))) {
        //     throw new AccessDeniedException("You are not authorized to check-in this appointment.");
        // }


        if (appointment.isCheckedIn()) {
            // It might be better to just return the existing link rather than throwing an error
            // if they try to check-in again. Depends on desired behavior.
            CheckInResponse response = new CheckInResponse();
            response.setAppointmentId(appointment.getId());
            response.setMeetingLink(appointment.getMeetingLink()); // Return existing link
            response.setCheckedIn(true);
            return response;
            // Or throw an error:
            // throw new IllegalStateException("This appointment has already been checked in.");
        }

        // Optional: Add time window check (e.g., only allow check-in within X minutes before/after the scheduled time)
        // Schedule schedule = appointment.getSchedule();
        // if (!isWithinCheckInWindow(schedule.getStartTime())) { // Need to add startTime to Schedule or derive it
        //     throw new BadRequestException("Check-in is not allowed at this time.");
        // }


        Account member = appointment.getAccount();
        Schedule schedule = appointment.getSchedule();
        Consultant consultant = schedule.getConsultant();

        if (consultant == null || consultant.getAccount() == null) {
            // This indicates an inconsistency in data. A booked appointment should have a consultant.
            throw new IllegalStateException("Consultant account not found for this schedule associated with the appointment");
        }

        Account consultantAccount = consultant.getAccount();

        // Create meeting link
        // You might want a more unique room name, perhaps including appointment ID or user IDs
        String meetingLink = jitsiService.createMeetingRoom("appointment-" + appointmentId); // Example: Use appointment ID


        // Gửi email
        try {
            emailService.sendMeetingLink(member.getEmail(), member.getName(), meetingLink);
            // Check if consultant account exists before sending email
            if (consultantAccount.getEmail() != null && !consultantAccount.getEmail().isEmpty()) {
                emailService.sendMeetingLink(consultantAccount.getEmail(), consultantAccount.getName(), meetingLink);
            } else {
                System.err.println("Consultant email is missing for appointment ID: " + appointmentId); // Log warning
            }
        } catch (Exception e) {
            // Log the error but don't necessarily fail the check-in process if email sending fails
            System.err.println("Failed to send meeting link email for appointment ID " + appointmentId + ": " + e.getMessage());
            // Depending on requirements, you might throw a custom exception or just log and continue
        }


        // Cập nhật database
        appointment.setMeetingLink(meetingLink);
        appointment.setCheckedIn(true);
        // Optional: Update status to IN_PROGRESS or similar
        // appointment.setStatus(AppointmentStatus.IN_PROGRESS);
        appointmentRepository.save(appointment);

        // Trả về DTO
        CheckInResponse response = new CheckInResponse();
        response.setAppointmentId(appointment.getId());
        response.setMeetingLink(meetingLink);
        response.setCheckedIn(true);

        return response;
    }


    // Phương thức để lấy Appointment theo ID và trả về DTO
    public AppointmentResponse getAppointmentById(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id " + appointmentId));

        // Sử dụng phương thức ánh xạ private
        return mapToAppointmentResponse(appointment);
    }

    // Phương thức mới để lấy TẤT CẢ Appointment và trả về List DTO
    public List<AppointmentResponse> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll(); // Lấy tất cả entities

        // Sử dụng Stream để ánh xạ từng entity sang DTO
        return appointments.stream()
                .map(this::mapToAppointmentResponse) // Ánh xạ từng Appointment dùng phương thức private
                .collect(Collectors.toList()); // Thu thập kết quả vào List
    }

    // --- PHƯƠNG THỨC MỚI ĐƯỢC THÊM: LẤY APPOINTMENTS THEO ACCOUNT ID ---
    /**
     * Retrieves a list of appointments for a specific account.
     *
     * @param accountId The ID of the account.
     * @return A list of AppointmentResponse DTOs.
     */
    public List<AppointmentResponse> getAppointmentsByAccountId(Long accountId) {
        // Lấy danh sách Appointment entities từ repository bằng accountId
        // Assuming AppointmentRepository has a method findByAccountId(Long accountId)
        List<Appointment> appointments = appointmentRepository.findByAccountId(accountId);

        // Sử dụng Stream để ánh xạ từng entity sang DTO AppointmentResponse
        return appointments.stream()
                .map(this::mapToAppointmentResponse) // Ánh xạ từng Appointment dùng phương thức private
                .collect(Collectors.toList()); // Thu thập kết quả vào List
    }
    // ------------------------------------------------------------------


    // Phương thức private để ánh xạ Appointment entity sang AppointmentResponse DTO
    private AppointmentResponse mapToAppointmentResponse(Appointment appointment) {
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setCreateAt(appointment.getCreateAt());
        response.setDescription(appointment.getDescription());
        response.setStatus(appointment.getStatus());
        response.setMeetingLink(appointment.getMeetingLink());
        response.setCheckedIn(appointment.isCheckedIn());

        // Lấy thông tin từ Account liên quan
        if (appointment.getAccount() != null) {
            response.setAccountId(appointment.getAccount().getId());
        }

        // Lấy thông tin từ Schedule liên quan
        if (appointment.getSchedule() != null) {
            response.setAppointmentDate(appointment.getSchedule().getDate());

            // Lấy scheduleId
            response.setScheduleId(appointment.getSchedule().getId()); // <--- THÊM DÒNG NÀY

            // Lấy consultantId từ schedule
            if (appointment.getSchedule().getConsultant() != null) {
                response.setConsultantId(appointment.getSchedule().getConsultant().getId());
            } else {
                response.setConsultantId(null);
            }
        } else {
            // Đặt giá trị mặc định nếu không có schedule
            response.setConsultantId(null);
            response.setScheduleId(null);
        }

        return response;
    }
    // -----------------------------------------------------------------------------------
}