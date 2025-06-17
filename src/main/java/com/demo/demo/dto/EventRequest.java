
package com.demo.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventRequest {
    private String title;
    private String description;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
