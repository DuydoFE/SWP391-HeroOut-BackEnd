package com.demo.demo.mapper;

import com.demo.demo.dto.EventRequest;
import com.demo.demo.entity.Event;
import org.modelmapper.PropertyMap;

public class EventMapper extends PropertyMap<EventRequest, Event> {
    @Override
    protected void configure() {
        map().setId(0);
        map().setTitle(source.getName());
        map().setDescription(source.getDescription());
        map().setLocation(source.getLocation());
        map().setStartTime(source.getStartTime());
        map().setEndTime(source.getEndTime());

    }
}
