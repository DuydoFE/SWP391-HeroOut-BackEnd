package com.demo.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Chapter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private String title;
    private String content;
    private int sequence;
}
