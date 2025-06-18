package com.demo.demo.dto;

import com.demo.demo.enums.Gender;
import com.demo.demo.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class AccountResponse {
    public long id;
    public String email;
    public String phone;
    public String name;
    public Gender gender;
    public Role role;
    public String token;
}
