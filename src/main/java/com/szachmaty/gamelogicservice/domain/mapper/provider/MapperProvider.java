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
                    if(GameWPlDTO.class.isAssignableFrom(clazz)) {
                        mapper.map(src -> src.getWhiteUser().getUsername(),
                                (dest , val) -> ((GameWPlDTO) dest).getWPlDTO().setUsername((String) val));
                    } else if(GameBPlDTO.class.isAssignableFrom(clazz)) {
                        mapper.map(src -> src.getBlackUser().getUsername(),
                                (dest,val) -> ((GameBPlDTO) dest).getBPlDTO().setUsername((String)val));
                    } else {
                        throw new IncorrectConvertTypeException("Cannot convert from: " + GameEntity.class + " to" + clazz +
                                " because GameStateDTO is not a superclass of " + clazz);
                    }
                });

        return modelMapper;
    }

    @Override
    public ModelMapper gameDTOEntityMapper(Class<? extends GameStateDTO> clazz) {
        ModelMapper modelMapper = modelMapper();
        modelMapper.typeMap(clazz, GameEntity.class)
                .addMappings(mapper -> {
                    if(GameWPlDTO.class.isAssignableFrom(clazz)) {
                        mapper.map(src -> ((GameWPlDTO)src).getWPlDTO().getUsername(),
                                (dest , val) -> dest.getWhiteUser().setUsername((String) val));
                    } else if(GameBPlDTO.class.isAssignableFrom(clazz)) {
                        mapper.map(src -> ((GameBPlDTO)src).getBPlDTO().getUsername(),
                                (dest,val) ->  dest.getBlackUser().setUsername((String)val));
                    } else {
                        throw new IncorrectConvertTypeException("Cannot convert from: " + clazz + " to" + GameEntity.class +
                                " because GameStateDTO is not a superclass of " + clazz);
                    }
                });
        return modelMapper;
    }

    private ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
