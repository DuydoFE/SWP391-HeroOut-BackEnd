package com.demo.demo.api;

import com.demo.demo.dto.ScheduleResponse;
import com.demo.demo.service.ScheduleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@SecurityRequirement(name = "api")
public class ScheduleAPI {

    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleAPI(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // ✅ GET /api/schedules → trả về tất cả lịch dạng DTO
    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getAllSchedules() {
        List<ScheduleResponse> schedules = scheduleService.getAllSchedules();
        return ResponseEntity.ok(schedules);
    }

    // ✅ GET /api/schedules/consultant/{consultantId} → lịch theo consultant
    @GetMapping("/consultant/{consultantId}")
    public ResponseEntity<List<ScheduleResponse>> getSchedulesByConsultantId(@PathVariable long consultantId) {
        List<ScheduleResponse> schedules = scheduleService.getSchedulesByConsultantId(consultantId);
        return ResponseEntity.ok(schedules);
    }

    // ✅ GET /api/schedules/{id} → lấy 1 lịch theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponse> getScheduleById(@PathVariable long id) {
        return scheduleService.getScheduleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
