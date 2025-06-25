package com.demo.demo.mapper;

import com.demo.demo.dto.CourseResponse;
import com.demo.demo.entity.Course;
import org.modelmapper.PropertyMap;

public class CourseMapper extends PropertyMap<Course, CourseResponse> {
    @Override
    protected void configure() {
        skip(destination.getId());
        map().setTitle(source.getTitle());
        map().setDescription(source.getDescription());
        map().setObjectives(source.getObjectives());
        map().setOverview(source.getOverview());
        map().setAgeGroup(source.getAgeGroup());
        map().setCreatedAt(source.getCreatedAt());
    }
}




