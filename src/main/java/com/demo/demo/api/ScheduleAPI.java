// src/main/java/com/demo/demo/api/ScheduleAPI.java
package com.demo.demo.api;

import com.demo.demo.dto.ScheduleResponseDto; // Import DTO
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

    // Endpoint to get all schedules, returning DTOs
    // GET /api/schedules
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> getAllSchedules() { // Kiểu trả về là List of DTOs
        List<ScheduleResponseDto> schedules = scheduleService.getAllSchedules(); // Gọi service trả về DTO
        return ResponseEntity.ok(schedules); // Return 200 OK with the list of DTOs
    }

    // Endpoint to get schedules by Consultant ID, returning DTOs
    // GET /api/schedules/consultant/{consultantId}
    // Đã sửa lại path và tên biến cho rõ ràng như lần sửa trước
    @GetMapping("/consultant/{consultantId}")
    public ResponseEntity<List<ScheduleResponseDto>> getSchedulesByConsultantId( // Kiểu trả về là List of DTOs
                                                                                 @PathVariable long consultantId) {

        List<ScheduleResponseDto> schedules = scheduleService.getSchedulesByConsultantId(consultantId); // Gọi service trả về DTO

        // Trả về 200 OK với danh sách DTO. Nếu không tìm thấy lịch trình nào cho consultant,
        // nó sẽ trả về một danh sách rỗng ([]), đây là một phản hồi thành công chuẩn.
        return ResponseEntity.ok(schedules);
    }


    // Optional: Endpoint để lấy một lịch trình cụ thể theo ID của nó, trả về DTO
    // GET /api/schedules/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> getScheduleById(@PathVariable long id) { // Kiểu trả về là DTO
        ScheduleResponseDto schedule = scheduleService.getScheduleById(id); // Gọi service trả về DTO
        if (schedule == null) {
            return ResponseEntity.notFound().build(); // Trả về 404 Not Found nếu không tìm thấy
        }
        return ResponseEntity.ok(schedule); // Trả về 200 OK với DTO Schedule
    }
}