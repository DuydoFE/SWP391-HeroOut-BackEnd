package com.demo.demo.entity;
import com.demo.demo.enums.Gender;
import com.demo.demo.enums.Role;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
public class Account implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    public String email;
    public String phone;
    public String password;
    public String fullName;


    @Enumerated(EnumType.STRING)
    public Gender gender;

    @Enumerated(EnumType.STRING)
    public Role role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}

//3 tier
// api(controller) : dieu phoi, nhan request FE
//service: xu li logic
//repository: tuong tac voi db
