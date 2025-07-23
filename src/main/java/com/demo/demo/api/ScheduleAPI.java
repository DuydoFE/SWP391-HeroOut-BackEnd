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


    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> getAllSchedules() {
        List<ScheduleResponseDto> schedules = scheduleService.getAllSchedules();
        return ResponseEntity.ok(schedules);
    }


    @GetMapping("/consultant/{consultantId}")
    public ResponseEntity<List<ScheduleResponseDto>> getSchedulesByConsultantId(@PathVariable long consultantId) {
        List<ScheduleResponseDto> schedules = scheduleService.getSchedulesByConsultantId(consultantId);
        return ResponseEntity.ok(schedules);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> getScheduleById(@PathVariable long id) {
        ScheduleResponseDto schedule = scheduleService.getScheduleById(id);
        if (schedule == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(schedule);
    }
}