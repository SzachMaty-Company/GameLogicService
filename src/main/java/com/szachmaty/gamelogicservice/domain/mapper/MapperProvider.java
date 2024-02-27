package com.szachmaty.gamelogicservice.domain.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MapperProvider implements Mapper {

    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
