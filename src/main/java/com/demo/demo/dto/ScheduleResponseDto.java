// src/main/java/com/demo/demo/dto/ScheduleResponseDto.java
package com.demo.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // Cần cập nhật constructor này sau khi thay đổi các trường
public class ScheduleResponseDto {
    private long id;
    private LocalDate date;
    private String recurrence;

    // Thay đổi kiểu dữ liệu và tên trường cho trạng thái booked
    private int bookedStatus; // <-- Đổi từ boolean booked sang int bookedStatus

    private Long slotId;

    private SlotDto slot;

    // Lưu ý: Constructor @AllArgsConstructor cần được cập nhật tự động bởi Lombok
    // để bao gồm trường int bookedStatus mới.
}