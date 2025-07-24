package com.demo.demo.entity;

import com.demo.demo.enums.EventParticipationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(nullable = true, columnDefinition = "DATETIME")
    private LocalDateTime checkInTime;

    @Column(nullable = true, columnDefinition = "DATETIME")
    private LocalDateTime checkOutTime;

    @Enumerated(EnumType.STRING)
    private EventParticipationStatus status;

    @OneToMany(mappedBy = "participation", cascade = CascadeType.ALL)
    private List<EventSurveyResponse> responses;

    private Integer totalScore;

    public EventParticipation(Event event, Account account) {
        this.event = event;
        this.account = account;
    }
}
