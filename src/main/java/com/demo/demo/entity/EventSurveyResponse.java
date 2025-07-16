package com.demo.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventSurveyResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "participation_id")
    private EventParticipation participation;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private EventSurveyQuestion question;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private EventSurveyOption selectedOption;
}
