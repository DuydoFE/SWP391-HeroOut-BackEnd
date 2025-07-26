// file: src/main/java/com/demo/demo/entity/Blog.java

package com.demo.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;


    private String image; // Trường này sẽ lưu URL của ảnh bìa từ Cloudinary

    private String description;
    private String author;
    private String readTime;
    private String views;
    private LocalDate date;
    private String tags;
}