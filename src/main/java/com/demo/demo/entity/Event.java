package com.demo.demo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, columnDefinition = "Nvarchar(200)")
    private String title;

    @Column(columnDefinition = "Nvarchar(200)")
    private String description;

    @Column(columnDefinition = "Nvarchar(200)")
    private String location;

    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime startTime;

    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime endTime;

    @OneToOne(mappedBy = "event")
    @JsonManagedReference
    private EventSurvey eventSurvey;

}
