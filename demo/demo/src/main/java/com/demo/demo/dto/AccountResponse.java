package com.demo.demo.dto;

import com.demo.demo.enums.Gender;
import com.demo.demo.enums.Role;
import jakarta.persistence.EnumType;
import lombok.Data;


@Data
public class AccountResponse {
    public String email;
    public String phone;
    public String fullName;

    public Gender gender;

    public Role role;

    public String token;

}
