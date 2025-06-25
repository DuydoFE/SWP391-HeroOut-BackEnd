package com.demo.demo.service;

import com.demo.demo.dto.RegisterSlotDTO;
import com.demo.demo.dto.ScheduleResponse;
import com.demo.demo.entity.Account;

import com.demo.demo.entity.Appointment;
import com.demo.demo.entity.Schedule;
import com.demo.demo.entity.Slot;
import com.demo.demo.enums.AppointmentStatus;
import com.demo.demo.exception.exceptions.BadRequestException;
import com.demo.demo.repository.AuthenticationRepository;
import com.demo.demo.repository.ScheduleRepository;
import com.demo.demo.repository.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SlotService {

    @Autowired
   SlotRepository slotRepository;

    @Autowired
    AuthenticationRepository authenticationRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    public List<Slot> get(){


        return slotRepository.findAll();
    }

    @Transactional
    public List<ScheduleResponse> registerSlot(RegisterSlotDTO dto) {
        Account account = authenticationRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        LocalDate date = dto.getDate();
        List<Long> slotIds = dto.getSlotIds();
        long consultantId = dto.getConsultantId();

        if (slotIds == null || slotIds.isEmpty()) {
            throw new BadRequestException("Phải chọn ít nhất một ca để đăng ký");
        }

        List<Schedule> schedules = scheduleRepository
                .findByConsultantIdAndDateAndSlotIdInAndIsBookedFalse(consultantId, date, slotIds);


        List<ScheduleResponse> responses = new ArrayList<>();

        for (Schedule schedule : schedules) {
            schedule.setBooked(true);

            Appointment appointment = new Appointment();
            appointment.setAccount(account);
            appointment.setSchedule(schedule);
            appointment.setCreateAt(LocalDate.now());
            appointment.setStatus(AppointmentStatus.BOOKED);

            schedule.setAppointment(appointment);

            scheduleRepository.save(schedule); // hoặc dùng appointmentRepository.save nếu không cascade

            ScheduleResponse response = new ScheduleResponse();
            response.setId(schedule.getId());
            response.setDate(schedule.getDate());
            response.setRecurrence(schedule.getRecurrence());
            response.setSlotId(schedule.getSlot().getId());
            response.setConsultantId(schedule.getConsultant().getId());
            response.setAppointmentId(schedule.getAppointment().getId()); // sau khi save mới an toàn

            responses.add(response);
        }

        return responses;
    }


    public void generateSlot() {
        // Check if slots already exist to avoid generating duplicates every time
        if (slotRepository.count() > 0) {
            // Slots already exist, maybe log or return early
            System.out.println("Slots already generated.");
            return;
        }

        LocalTime start = LocalTime.of(7, 0);
        LocalTime end = LocalTime.of(17, 0);
        List<Slot> slots = new ArrayList<>();

        while (start.isBefore(end)) {
            Slot slot = new Slot(); // <--- Creating new instances
            slot.setLabel(start.toString() + " - " + start.plusMinutes(30).toString());
            slot.setSlot_start(start);
            slot.setSlot_end(start.plusMinutes(30));

            slots.add(slot); // <--- Adding multiple new instances to a list
            start = start.plusMinutes(30);
        }

        slotRepository.saveAll(slots); // <--- Trying to save the list
    }

    public List<Slot> getAvailableSlots() {
        return slotRepository.findAllByIsDeletedFalse();
    }
}
