package com.demo.demo.mapper;

import com.demo.demo.dto.EventParticipationRequest;
import com.demo.demo.entity.Event;
import com.demo.demo.entity.EventParticipation;
import org.modelmapper.AbstractConverter;
import org.modelmapper.PropertyMap;

public class EventParticipationMapper extends PropertyMap<EventParticipationRequest, EventParticipation> {

    @Override
    protected void configure() {
        // Gán các field thông thường
        map().setCheckInTime(source.getCheckInTime());
        map().setCheckOutTime(source.getCheckOutTime());
        map().setStatus(source.getStatus());


    }
}
