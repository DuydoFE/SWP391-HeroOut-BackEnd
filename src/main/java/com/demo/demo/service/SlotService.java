package com.demo.demo.service;

import com.demo.demo.dto.RegisterSlotDTO;
import com.demo.demo.entity.Consultant;
import com.demo.demo.entity.Schedule;
import com.demo.demo.entity.Slot;
import com.demo.demo.exception.exceptions.BadRequestException;
import com.demo.demo.repository.ConsultantRepository;
import com.demo.demo.repository.ScheduleRepository;
import com.demo.demo.repository.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import Transactional

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SlotService {

    @Autowired
    SlotRepository slotRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    ConsultantRepository consultantRepository;

    // Các phương thức khác giữ nguyên...
    public List<Slot> get() {
        return slotRepository.findByIsDeletedFalse();
    }

    // === VIẾT LẠI HOÀN TOÀN PHƯƠNG THỨC NÀY ===
    @Transactional // Đảm bảo tất cả các thao tác lưu thành công hoặc không có gì cả
    public List<Schedule> registerSlot(RegisterSlotDTO registerSlotDTO) {
        // 1. Kiểm tra đầu vào
        if (registerSlotDTO.getSlotIds() == null || registerSlotDTO.getSlotIds().isEmpty()) {
            throw new BadRequestException("Danh sách slotId không được để trống.");
        }

        // 2. Tìm Consultant
        Consultant consultant = consultantRepository.findById(registerSlotDTO.getConsultantId())
                .orElseThrow(() -> new BadRequestException("Không tìm thấy chuyên gia tư vấn với ID: " + registerSlotDTO.getConsultantId()));

        List<Schedule> newSchedules = new ArrayList<>();

        // 3. Lặp qua danh sách slotId được cung cấp
        for (Long slotId : registerSlotDTO.getSlotIds()) {
            // 4. Tìm từng Slot
            Slot slot = slotRepository.findById(slotId)
                    .orElseThrow(() -> new BadRequestException("Không tìm thấy slot với ID: " + slotId));

            // 5. Kiểm tra xem lịch hẹn đã tồn tại chưa để tránh trùng lặp
            Schedule existingSchedule = scheduleRepository.findScheduleBySlotIdAndConsultantAndDate(slotId, consultant, registerSlotDTO.getDate());
            if (existingSchedule != null) {
                // Bạn có thể bỏ qua lỗi này nếu muốn, hoặc báo lỗi để người dùng biết
                throw new BadRequestException("Lịch hẹn cho slot " + slotId + " vào ngày " + registerSlotDTO.getDate() + " đã tồn tại.");
            }

            // 6. Tạo Schedule mới và thêm vào danh sách
            Schedule schedule = new Schedule();
            schedule.setSlot(slot);
            schedule.setConsultant(consultant);
            schedule.setDate(registerSlotDTO.getDate());
            // bạn có thể đặt trạng thái mặc định ở đây, ví dụ: schedule.setStatus("AVAILABLE");
            newSchedules.add(schedule);
        }

        // 7. Lưu tất cả các schedule mới vào DB trong một lần gọi
        return scheduleRepository.saveAll(newSchedules);
    }


    public void generateSlot() {
        if (slotRepository.count() > 0) {
            System.out.println("Slots already generated.");
            return;
        }

        LocalTime start = LocalTime.of(7, 0);
        LocalTime end = LocalTime.of(17, 0);
        List<Slot> slots = new ArrayList<>();

        while (start.isBefore(end)) {
            Slot slot = new Slot();
            slot.setLabel(start.toString() + " - " + start.plusMinutes(30).toString());
            slot.setSlot_start(start);
            slot.setSlot_end(start.plusMinutes(30));
            slot.setDeleted(false);
            slots.add(slot);
            start = start.plusMinutes(30);
        }

        slotRepository.saveAll(slots);
    }
}