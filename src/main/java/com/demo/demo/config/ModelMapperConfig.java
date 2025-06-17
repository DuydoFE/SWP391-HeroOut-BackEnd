package com.demo.demo.config;

import com.demo.demo.mapper.EventMapper;
import com.demo.demo.mapper.EventParticipationMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new EventMapper());
        return modelMapper;
    }
}
