//package com.demo.demo.mapper;
//
//import com.demo.demo.dto.EventParticipationRequest;
//import com.demo.demo.entity.EventParticipation;
//import org.modelmapper.PropertyMap;
//import org.springframework.stereotype.Component;
//
//@Component
//public class EventParticipationMapper extends PropertyMap<EventParticipationRequest, EventParticipation> {
//
//    @Override
//    protected void configure() {
//        skip(destination.getId()); // bỏ qua ID để tránh ghi đè khi tạo mới
//        map().setAccount(source.getAccount());
//        map().setEvent(source.getEvent());
//        map().setCheckInTime(source.getCheckInTime());
//        map().setCheckOutTime(source.getCheckOutTime());
//        map().setStatus(source.getStatus());
//    }
//}
