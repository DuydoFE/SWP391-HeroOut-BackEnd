package com.demo.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UpdateAccountRequest {
    private String name;
    private String phone;
    private String address;
    private String avatar;
    private Date dateOfBirth;
}
