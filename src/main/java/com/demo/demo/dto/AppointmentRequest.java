package com.demo.demo.dto; // Đảm bảo đúng package

import java.time.LocalDate; // Giữ lại vì các trường khác còn dùng

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder; // Tùy chọn

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Nếu sử dụng Builder
public class AppointmentRequest {

    private Long consultantId;
    private LocalDate appointmentDate;
    private Long slotId;


    private Long scheduleId;


    private String description;


}