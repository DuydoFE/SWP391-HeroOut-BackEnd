package com.demo.demo.entity;

import com.demo.demo.enums.ProgressStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "EnrollmentChapter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentChapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "enrollment_id")
    private Enrollment enrollment;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    @Enumerated(EnumType.STRING)
    private ProgressStatus status;
}
