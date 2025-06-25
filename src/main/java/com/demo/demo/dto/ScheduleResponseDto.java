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
@AllArgsConstructor
public class ScheduleResponseDto {
    // Các thuộc tính từ Schedule Entity mà bạn muốn hiển thị
    private long id;
    private LocalDate date;
    private String recurrence;
    // Bỏ qua isBooked, consultant, appointments

    // Đối tượng Slot được biểu diễn bằng SlotDto
    private SlotDto slot;
}