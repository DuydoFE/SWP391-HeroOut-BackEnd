// src/main/java/com/demo/demo/dto/SlotDto.java
package com.demo.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime; // Đã sửa: Sử dụng LocalTime

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SlotDto {
    // Đã sửa: Các trường sử dụng LocalTime
    private LocalTime slotStart;
    private LocalTime slotEnd;

    // Constructor cũng sử dụng LocalTime
    // Lombok @AllArgsConstructor sẽ tự tạo constructor này
}