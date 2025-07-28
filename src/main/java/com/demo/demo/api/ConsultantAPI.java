package com.demo.demo.api;


import com.demo.demo.dto.ConsultantResponse; // Import ConsultantResponse DTO
import com.demo.demo.dto.ConsultantUpdateRequest;
import com.demo.demo.service.ConsultantService; // Import ConsultantService
import com.demo.demo.exception.exceptions.ResourceNotFoundException; // Import ResourceNotFoundException để xử lý 404
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // Import HttpStatus
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
// import java.util.Optional; // Không cần Optional khi Service ném Exception
import java.util.Arrays; // Keep if used elsewhere

@RestController
@SecurityRequirement(name = "api")
@RequestMapping("/api/consultants")
public class ConsultantAPI {


    private final ConsultantService consultantService; // Inject ConsultantService


    @Autowired
    public ConsultantAPI(ConsultantService consultantService) {
        this.consultantService = consultantService;
    }


    @GetMapping

    public ResponseEntity<List<ConsultantResponse>> getAllConsultants() {
        List<ConsultantResponse> consultantResponses = consultantService.getAllConsultants();
        return ResponseEntity.ok(consultantResponses);
    }

    @GetMapping("/{id}")

    public ResponseEntity<ConsultantResponse> getConsultantById(@PathVariable Long id) {
        try {
            ConsultantResponse consultantResponse = consultantService.getConsultantById(id); // Gọi Service
            return ResponseEntity.ok(consultantResponse); // Trả về DTO nếu tìm thấy
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Trả về 404 nếu không tìm thấy

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultantResponse> updateConsultant(
            @PathVariable Long id,
            @RequestBody ConsultantUpdateRequest updateRequest) {
        try {
            ConsultantResponse updatedConsultant = consultantService.updateConsultant(id, updateRequest);
            return ResponseEntity.ok(updatedConsultant); // Trả về 200 OK với dữ liệu đã cập nhật
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Trả về 404 nếu không tìm thấy
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}