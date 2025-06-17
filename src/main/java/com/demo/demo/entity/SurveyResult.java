package com.demo.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResult {
    @Id
    private long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private int score;
    private String riskLevel;
    private String recommendation;
    private LocalTime takenAt;


}
