package com.demo.demo.dto;

import com.demo.demo.entity.Consultant;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterSlotDTO {

    LocalDate date;
    long consultantId;

}
