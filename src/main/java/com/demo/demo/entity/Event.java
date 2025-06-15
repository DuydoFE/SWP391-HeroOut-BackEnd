package com.demo.demo.entity;

import jakarta.persistence.*;
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
    @Column(nullable = false, columnDefinition = "Nvarchar(200)")
    private String title;
    @Column(nullable = false, columnDefinition = "Nvarchar(200)")
    private String description;
    @Column(nullable = false, columnDefinition = "Nvarchar(200)")
    private String location;
    @Column(nullable = false,columnDefinition = "DATETIME")
    private LocalDateTime startTime;
    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime endTime;

}
