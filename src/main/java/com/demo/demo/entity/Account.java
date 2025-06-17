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
    private long id;

    private String email;
    private String phone;
    private String password;
    private String fullName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "account")
    private Consultant consultant;

    @OneToMany(mappedBy = "account")
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "account")
    private List<EventParticipation> eventParticipations;

    @OneToMany(mappedBy = "account")
    private List<SurveyResult> surveyResults;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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

// 3 tier
// api (controller): điều phối, nhận request của FE
// service: xử lý logic
// repository: tương tác với DB