package com.demo.demo.mapper;

import com.demo.demo.dto.EventRequest;
import com.demo.demo.entity.Event;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class EventMapper extends PropertyMap<EventRequest, Event> {
    @Override
    protected void configure() {
        skip(destination.getId());
        map().setTitle(source.getTitle());
        map().setDescription(source.getDescription());
        map().setLocation(source.getLocation());
        map().setStartTime(source.getStartTime());
        map().setEndTime(source.getEndTime());
    }
}
