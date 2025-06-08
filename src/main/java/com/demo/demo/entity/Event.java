package com.demo.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Event")
public class Event {
    @Id
    private long id;
    @Column(nullable = false, columnDefinition = "Nvarchar(200)")
    private String title;
    @Column(nullable = false, columnDefinition = "Nvarchar(200)")
    private String description;
    @Column(nullable = false, columnDefinition = "Nvarchar(200)")
    private String location;
    @Column(nullable = false)
    private String startTime;
    @Column(nullable = false)
    private String endTime;

}
