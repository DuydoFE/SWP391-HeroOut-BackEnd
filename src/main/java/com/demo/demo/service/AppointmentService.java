package com.demo.demo.service;

import com.demo.demo.dto.AppointmentRequest;
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



    @Transactional
    public Appointment create(AppointmentRequest appointmentRequest) {

        //find doctor
        Account consultant = authenticationRepository.findById(appointmentRequest.getConsultantId()).orElseThrow(()->new RuntimeException("consultantnotfound"));

        //check doctor
        if (!consultant.getRole().equals(Role.CONSULTANT)){
            throw new BadRequestException("account is not a consultant");
        }

        //tim slot
       Schedule slot = scheduleRepository.findScheduleBySlotIdAndAccountAndDate(
                appointmentRequest.getSlotId(),
                consultant,
                appointmentRequest.getAppointmentDate()
        );
        //check xem slot da dat hay chua
        if(!slot.isBooked()){
            throw new BadRequestException("slot is not available");
        }




        //lay tai khoan
        Account currentAccount = authenticationService.getCurrentAccount();


        //APPONTMENT
        Appointment appointment = new Appointment();
        appointment.setCreateAt(LocalDate.now());
        appointment.setStatus(AppointmentStatus.BOOKED);
        appointment.setAccount(currentAccount);
        appointmentRepository.save(appointment);

        //set slot do thanh da dat

        slot.setBooked(false);


        return appointment;
    }
}
