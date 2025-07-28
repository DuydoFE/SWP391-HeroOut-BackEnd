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

    private LocalTime slotStart;
    private LocalTime slotEnd;

}