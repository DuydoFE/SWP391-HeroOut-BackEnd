package com.demo.demo.api;

import com.demo.demo.entity.Schedule;
import com.demo.demo.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Indicates this is a REST controller
@RequestMapping("/api/schedules") // Base path for all endpoints in this controller
public class ScheduleAPI {

    private final ScheduleService scheduleService;

    // Constructor injection for the service
    @Autowired
    public ScheduleAPI(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // Endpoint to get all schedules
    // GET /api/schedules
    @GetMapping
    public ResponseEntity<List<Schedule>> getAllSchedules() {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        return ResponseEntity.ok(schedules); // Return 200 OK with the list of schedules
    }

    // Endpoint to get schedules by account ID
    // GET /api/schedules/account/{accountId}
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Schedule>> getSchedulesByAccountId(@PathVariable long accountId) {
        List<Schedule> schedules = scheduleService.getSchedulesByAccountId(accountId);
        // Return 200 OK with the list of schedules. If no schedules are found for the account,
        // this will return an empty list, which is a standard successful response.
        return ResponseEntity.ok(schedules);
    }


}