package com.demo.demo.dto;

import com.demo.demo.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private long id;
    private LocalDate createAt;
    private String description;
    private AppointmentStatus status;
    private Long accountId;     // Thêm accountId để biết Appointment này của Account nào
    private Long consultantId;  // Trường mới được yêu cầu

    // Constructor hoặc builder nếu cần mapping phức tạp
    // Có thể thêm các trường khác từ Account hoặc Schedule nếu muốn hiển thị
}