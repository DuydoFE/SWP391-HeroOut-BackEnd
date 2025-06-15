package com.demo.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class EventRequest {
    public long id;
    public String name;
    public String description;
    public String location;
    public LocalDateTime startTime;
    public LocalDateTime endTime;

}
