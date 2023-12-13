package com.szachmaty.gamelogicservice.domain.mapper;

import com.szachmaty.gamelogicservice.domain.dto.GameStateDTO;
import com.szachmaty.gamelogicservice.domain.dto.GameWPlDTO;
import org.modelmapper.ModelMapper;

public interface Mapper {
    ModelMapper gameEntityDTOMapper(Class<? extends GameStateDTO> clazz);

    ModelMapper gameDTOEntityMapper(Class<? extends GameStateDTO> clazz);
}
