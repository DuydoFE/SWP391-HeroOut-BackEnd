package com.demo.demo.api;

import com.demo.demo.dto.ScheduleRequest;
import com.demo.demo.dto.ScheduleResponse;
import com.demo.demo.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleAPI {

    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleAPI(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // 🔵 Lấy tất cả schedules
    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.getAllSchedules());
    }

    // 🔵 Lấy schedule theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponse> getScheduleById(@PathVariable Long id) {
        return scheduleService.getScheduleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔵 Lấy schedule theo consultantId
    @GetMapping("/consultant/{consultantId}")
    public ResponseEntity<List<ScheduleResponse>> getByConsultant(@PathVariable Long consultantId) {
        return ResponseEntity.ok(scheduleService.getSchedulesByConsultantId(consultantId));
    }

    // 🟢 Tạo schedule mới
    @PostMapping
    public ResponseEntity<ScheduleResponse> createSchedule(@RequestBody @Valid ScheduleRequest request) {
        return ResponseEntity.ok(scheduleService.createSchedule(request));
    }

    // 🟡 Cập nhật schedule
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponse> updateSchedule(
            @PathVariable Long id,
            @RequestBody @Valid ScheduleRequest request
    ) {
        return ResponseEntity.ok(scheduleService.updateSchedule(id, request));
    }

    // 🔴 Xoá schedule
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
