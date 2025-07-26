package com.demo.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BlogRequest {

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Author is required")
    private String author;

    private String readTime;

    private String views;

    private LocalDate date;

    private String image;

    private String tags;
}
