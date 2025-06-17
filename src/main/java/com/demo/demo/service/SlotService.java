package com.demo.demo.service;

import com.demo.demo.dto.RegisterSlotDTO;
import com.demo.demo.entity.Account;

import com.demo.demo.entity.Schedule;
import com.demo.demo.entity.Slot;
import com.demo.demo.exception.exceptions.BadRequestException;
import com.demo.demo.repository.AuthenticationRepository;
import com.demo.demo.repository.ScheduleRepository;
import com.demo.demo.repository.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Schedule> registerSlot(RegisterSlotDTO registerSlotDTO) {
        Account account = authenticationRepository.findById(registerSlotDTO.getAcocuntId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        List<Schedule> schedules = new ArrayList<>();

        // Nếu muốn kiểm tra xem đã có lịch trong ngày chưa, bỏ comment dưới đây
    /*
    List<Schedule> oldSchedules = scheduleRepository.findScheduleByAccountAndDate(account, registerSlotDTO.getDate());
    if (!oldSchedules.isEmpty()) {
        throw new BadRequestException("Đã có ca trong ngày này");
    }
    */

        for (Slot slot : slotRepository.findAll()) {
            Schedule schedule = new Schedule();
            schedule.setSlot(slot);
            schedule.setAccount(account);
            schedule.setDate(registerSlotDTO.getDate());
            schedules.add(schedule); // ✅ Sửa ở đây
        }

        return scheduleRepository.saveAll(schedules); // Trả về danh sách Schedule đã lưu
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

            // ID is likely not set here, or defaults to 0 if using primitive long

            slots.add(slot); // <--- Adding multiple new instances to a list
            start = start.plusMinutes(30);
        }

        slotRepository.saveAll(slots); // <--- Trying to save the list
    }
}
