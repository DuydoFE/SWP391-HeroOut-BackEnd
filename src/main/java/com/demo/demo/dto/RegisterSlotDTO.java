package com.demo.demo.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class RegisterSlotDTO {
    private LocalDate date;
    private long accountId;
    private long consultantId;
    private List<Long> slotIds; // Cho phép đăng ký 1-n slots cụ thể
}