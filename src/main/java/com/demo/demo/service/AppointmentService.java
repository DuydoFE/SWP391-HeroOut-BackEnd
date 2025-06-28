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

        if (appointment.isCheckedIn()) {
            throw new IllegalStateException("This appointment has already been checked in.");
        }

        Account member = appointment.getAccount();
        Schedule schedule = appointment.getSchedule();
        Consultant consultant = schedule.getConsultant();

        if (consultant == null || consultant.getAccount() == null) {
            throw new IllegalStateException("Consultant account not found for this schedule");
        }

        Account consultantAccount = consultant.getAccount();

        String meetingLink = jitsiService.createMeetingRoom(member.getName());

        // Gửi email
        emailService.sendMeetingLink(member.getEmail(), member.getName(), meetingLink);
        emailService.sendMeetingLink(consultantAccount.getEmail(), consultantAccount.getName(), meetingLink);

        // Cập nhật database
        appointment.setMeetingLink(meetingLink);
        appointment.setCheckedIn(true);
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

    // --- Phương thức mới để lấy TẤT CẢ Appointment và trả về List DTO ---
    public List<AppointmentResponse> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll(); // Lấy tất cả entities

        // Sử dụng Stream để ánh xạ từng entity sang DTO
        return appointments.stream()
                .map(this::mapToAppointmentResponse) // Ánh xạ từng Appointment dùng phương thức private
                .collect(Collectors.toList()); // Thu thập kết quả vào List
    }
    // --------------------------------------------------------------

    // --- Phương thức private để ánh xạ Appointment entity sang AppointmentResponse DTO ---
    private AppointmentResponse mapToAppointmentResponse(Appointment appointment) {
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setCreateAt(appointment.getCreateAt());
        response.setDescription(appointment.getDescription());
        response.setStatus(appointment.getStatus());

        if (appointment.getAccount() != null) {
            response.setAccountId(appointment.getAccount().getId());
        }

        // Lấy consultantId từ schedule
        if (appointment.getSchedule() != null && appointment.getSchedule().getConsultant() != null) {
            response.setConsultantId(appointment.getSchedule().getConsultant().getId());
        } else {
            response.setConsultantId(null); // Hoặc giá trị mặc định khác nếu cần
        }

        return response;
    }
    // -----------------------------------------------------------------------------------
}