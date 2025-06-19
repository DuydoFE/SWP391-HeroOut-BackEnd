package com.demo.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChapterDTO {
    private Long id;
    private Long courseId;
    private String title;
    private String content;
    private int sequence;
}
