package com.demo.demo.dto;

import com.demo.demo.enums.Gender;
import com.demo.demo.enums.Role;
import com.demo.demo.enums.AccountStatus; // Thêm import này
// import jakarta.persistence.EnumType; // Not typically needed in DTO
// import jakarta.persistence.Enumerated; // Not typically needed in DTO
import lombok.Data;

@Data
public class AccountResponse {
    public long id;
    public String email;
    public String phone;
    public String name;
    public Gender gender;
    public Role role;
    public AccountStatus status;
    public String token;

}