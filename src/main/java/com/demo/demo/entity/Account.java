package com.demo.demo.entity;

import com.demo.demo.enums.Gender;
import com.demo.demo.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


@Entity
@Getter
@Setter
public class Account implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String name;
    public  String email;
    public String phone;
    private String password;
    public String address;
    public String avatar;


    @Temporal(TemporalType.DATE) // Use TemporalType.DATE if you only need the date part (day, month, year)
    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Consultant> consultants = new HashSet<>();

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    List<Schedule> schedules;

    @OneToMany(mappedBy = "account")
    @JsonIgnore
   List<Appointment> appointments;

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