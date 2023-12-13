package com.szachmaty.gamelogicservice.domain.mapper.provider;

import com.szachmaty.gamelogicservice.domain.dto.GameBPlDTO;
import com.szachmaty.gamelogicservice.domain.dto.GameStateDTO;
import com.szachmaty.gamelogicservice.domain.dto.GameWPlDTO;
import com.szachmaty.gamelogicservice.domain.entity.GameEntity;
import com.szachmaty.gamelogicservice.domain.mapper.Mapper;
import com.szachmaty.gamelogicservice.domain.repository.exception.IncorrectConvertTypeException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MapperProvider implements Mapper {

    @Override
    public ModelMapper gameEntityDTOMapper(Class<? extends GameStateDTO> clazz) {
        ModelMapper modelMapper = modelMapper();
        modelMapper.typeMap(GameEntity.class, clazz)
                .addMappings(mapper -> {
                    if(clazz.isAssignableFrom(GameWPlDTO.class)) {
                        mapper.map(src -> src.getWhiteUser().getUsername(),
                                (dest , val) -> ((GameWPlDTO) dest).getWPlDTO().setUsername((String) val));
                    } else if(clazz.isAssignableFrom(GameBPlDTO.class)) {
                        mapper.map(src -> src.getBlackUser().getUsername(),
                                (dest,val) -> ((GameBPlDTO) dest).getBPlDTO().setUsername((String)val));
                    } else {
                        throw new IncorrectConvertTypeException("Cannot convert from: " + GameEntity.class + " to" + clazz +
                                " beacause GameStateDTO is not a superclass of " + clazz);
                    }
                });

        return modelMapper;
    }

    private ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
