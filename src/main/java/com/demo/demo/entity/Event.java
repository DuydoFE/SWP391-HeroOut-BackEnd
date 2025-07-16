package com.demo.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

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

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must be at most 200 characters")
    @Column(nullable = false, columnDefinition = "Nvarchar(200)")
    private String title;

    @Size(max = 200, message = "Description must be at most 200 characters")
    @Column(columnDefinition = "Nvarchar(200)")
    private String description;

    @Size(max = 200, message = "Location must be at most 200 characters")
    @Column(columnDefinition = "Nvarchar(200)")
    private String location;

    @NotNull    (message = "Start time is required")
    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime endTime;

    @OneToOne(mappedBy = "event", cascade = CascadeType.ALL)
    private EventSurvey eventSurvey;
}
