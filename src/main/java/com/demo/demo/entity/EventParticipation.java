package com.demo.demo.entity;

import com.demo.demo.enums.EventParticipationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime checkInTime;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime checkOutTime;
    @Enumerated(EnumType.STRING)
    private EventParticipationStatus status;

}
