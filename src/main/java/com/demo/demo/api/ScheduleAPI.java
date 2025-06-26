// src/main/java/com/demo/demo/api/ScheduleAPI.java
package com.demo.demo.api;

import com.demo.demo.dto.ScheduleResponseDto;
import com.demo.demo.service.ScheduleService;
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

    // Endpoint to get all schedules, returning DTOs
    // GET /api/schedules
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> getAllSchedules() {
        List<ScheduleResponseDto> schedules = scheduleService.getAllSchedules();
        return ResponseEntity.ok(schedules);
    }

    // Endpoint to get schedules by Consultant ID, returning DTOs
    // GET /api/schedules/consultant/{consultantId}
    @GetMapping("/consultant/{consultantId}")
    public ResponseEntity<List<ScheduleResponseDto>> getSchedulesByConsultantId(@PathVariable long consultantId) {
        List<ScheduleResponseDto> schedules = scheduleService.getSchedulesByConsultantId(consultantId);
        return ResponseEntity.ok(schedules);
    }

    // Optional: Endpoint để lấy một lịch trình cụ thể theo ID của nó, trả về DTO
    // GET /api/schedules/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> getScheduleById(@PathVariable long id) {
        ScheduleResponseDto schedule = scheduleService.getScheduleById(id);
        if (schedule == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(schedule);
    }
}