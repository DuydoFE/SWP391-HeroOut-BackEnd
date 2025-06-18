package com.demo.demo.dto;

import com.demo.demo.enums.Gender;
import com.demo.demo.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AccountRequest {
    private long id;
    private String email;
    private String phone;
    private String password;
    private String name;
    private String address;
    private String avatar;
    private Date dateOfBirth;
    private Gender gender;
    private Role role;
}
