package com.demo.demo.dto;

import com.demo.demo.entity.Consultant;
import lombok.Data;

import java.time.LocalDate;
import java.util.List; // Import List

@Data
public class RegisterSlotDTO {

    LocalDate date;
    long consultantId;
    List<Long> slotIds;

}