package com.demo.demo.service;

import com.demo.demo.dto.AppointmentRequest;
import com.demo.demo.entity.Account;
import com.demo.demo.entity.Appointment;
import com.demo.demo.entity.Consultant;
import com.demo.demo.entity.Schedule;
import com.demo.demo.enums.AppointmentStatus;
import com.demo.demo.enums.Role;
import com.demo.demo.exception.exceptions.BadRequestException;
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

@Service
public class AppointmentService {
    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired(required = false) // Make it not required if not used
    AuthenticationRepository authenticationRepository; // Still marked as unused

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
        Consultant consultant = consultantRepository.findById(appointmentRequest.getConsultantId()).orElseThrow(()->new RuntimeException("consultantnotfound"));

        //check doctor
        if (!consultant.getAccount().getRole().equals(Role.CONSULTANT)){
            throw new BadRequestException("account is not a consultant");
        }

        //tim slot
        // Note: scheduleRepository.findScheduleBySlotIdAndConsultantAndDate is assumed to exist and return a Schedule
        Schedule slot = scheduleRepository.findScheduleBySlotIdAndConsultantAndDate(
                appointmentRequest.getSlotId(),
                consultant,
                appointmentRequest.getAppointmentDate()
        );
        //check xem slot da dat hay chua
        // Note: isBooked() returning true means it's *available* based on the logic below.
        // If isBooked() means it's *already* booked, the check should be `if (slot.isBooked())`.
        if(!slot.isBooked()){ // This seems to mean "if it's NOT available (i.e., booked)"
            // Correcting the logic based on the original comment "check xem slot da dat hay chua" and the subsequent slot.setBooked(false)
            // If isBooked() == true means "available", the check should be if (!slot.isBooked()) throw BadRequest
            // If isBooked() == true means "booked", the check should be if (slot.isBooked()) throw BadRequest
            // Assuming isBooked() == true means "available" based on the code's flow
            // So the original logic `if(!slot.isBooked())` means `if (slot is NOT available)`
            throw new BadRequestException("slot is not available");
        }


        //lay tai khoan
        Account currentAccount = authenticationService.getCurrentAccount();


        //APPONTMENT
        Appointment appointment = new Appointment();
        appointment.setCreateAt(LocalDate.now());
        appointment.setStatus(AppointmentStatus.BOOKED);
        appointment.setAccount(currentAccount);
        appointment.setSchedule(slot);
        // Lấy description từ request và đặt vào entity
        appointment.setDescription(appointmentRequest.getDescription()); // <-- Dòng thêm vào
        appointmentRepository.save(appointment);

        //set slot do thanh da dat
        // Note: This line makes the slot *not* booked (booked=false) after booking.
        // This seems counter-intuitive. Usually, booking marks a slot as booked (booked=true).
        // Please double-check the intended logic for the `isBooked` flag.
        slot.setBooked(false); // <-- Check this logic

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
}