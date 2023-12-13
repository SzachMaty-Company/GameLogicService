package com.szachmaty.gamelogicservice.domain.mapper.converter;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.szachmaty.gamelogicservice.application.game.Game;
import com.szachmaty.gamelogicservice.application.manager.GameDTOManager;
import com.szachmaty.gamelogicservice.domain.dto.GameBPlDTO;
import com.szachmaty.gamelogicservice.domain.dto.GameStateDTO;
import com.szachmaty.gamelogicservice.domain.dto.GameWPlDTO;
import com.szachmaty.gamelogicservice.domain.dto.UserDTO;
import com.szachmaty.gamelogicservice.domain.entity.GameEntity;
import com.szachmaty.gamelogicservice.domain.entity.UserEntity;
import com.szachmaty.gamelogicservice.domain.entity.enumeration.GameStatus;
import com.szachmaty.gamelogicservice.domain.mapper.Mapper;
import com.szachmaty.gamelogicservice.domain.mapper.provider.MapperProvider;
import com.szachmaty.gamelogicservice.domain.repository.GameEntityDao;
import com.szachmaty.gamelogicservice.domain.repository.exception.GameEntityDTOConversionException;
import com.szachmaty.gamelogicservice.domain.repository.exception.GameEntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class EntityDTOConverter implements GameDTOManager {

    private final GameEntityDao gameEntityDao;
    private final Mapper mapperProvider;

    @Autowired
    public EntityDTOConverter(GameEntityDao gameEntityDao, Mapper mapperProvider) {
        this.gameEntityDao = gameEntityDao;
        this.mapperProvider = mapperProvider;
    }

    @Override
    public GameWPlDTO getGameStateWPlById(long gameId) {
        GameEntity game = gameEntityDao.findGameById(gameId);
        if(game == null) {
            throw new GameEntityNotFoundException("Cannot find GameEntity with given id:" + gameId);
        }
        ModelMapper modelMapper = mapperProvider.gameEntityDTOMapper(GameWPlDTO.class);
        GameWPlDTO gameWPlDTO = modelMapper.map(game, GameWPlDTO.class);
        if(gameWPlDTO == null) {
            throw new GameEntityDTOConversionException("Cannot convert: " + GameEntity.class + "to " + GameWPlDTO.class);
        }
        return gameWPlDTO;
    }

    @Override
    public GameBPlDTO getGameStateBPlById(long gameId) {
        GameEntity game = gameEntityDao.findGameById(gameId);
        if(game == null) {
            throw new GameEntityNotFoundException("Cannot find GameEntity with given id:" + gameId);
        }
        ModelMapper modelMapper = mapperProvider.gameEntityDTOMapper(GameBPlDTO.class);
        GameBPlDTO gameBPlDTO = modelMapper.map(game, GameBPlDTO.class);
        if(gameBPlDTO == null) {
            throw new GameEntityDTOConversionException("Cannot convert: " + GameEntity.class + "to " + GameBPlDTO.class);
        }
        return gameBPlDTO;
    }

    @Override
    public void saveGameStateWPl(GameWPlDTO gameWPlDTO) {

    }

    @Override
    public void saveGameStateBPl(GameBPlDTO gameBPlDTO) {

    }

}
