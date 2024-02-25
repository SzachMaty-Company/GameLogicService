package com.szachmaty.gamelogicservice.domain.mapper.provider;

import com.szachmaty.gamelogicservice.domain.dto.GameBPlDTO;
import com.szachmaty.gamelogicservice.domain.dto.GameStateDTO;
import com.szachmaty.gamelogicservice.domain.dto.GameWPlDTO;
import com.szachmaty.gamelogicservice.domain.dto.UserDTO;
import com.szachmaty.gamelogicservice.domain.entity.GameEntity;
import com.szachmaty.gamelogicservice.domain.entity.MoveEntity;
import com.szachmaty.gamelogicservice.domain.entity.UserEntity;
import com.szachmaty.gamelogicservice.domain.entity.enumeration.GameStatus;
import com.szachmaty.gamelogicservice.domain.mapper.Mapper;
import com.szachmaty.gamelogicservice.application.repository.exception.IncorrectConvertTypeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapperProviderTest {

    private Mapper mapperProvider;
    private GameEntity game;

    @BeforeEach
    public void setup() {
        mapperProvider = new MapperProvider();

        List<MoveEntity> moveList = new LinkedList<>();
        moveList.add(new MoveEntity(1, "WA1"));
        game = GameEntity.builder()
                .whiteUser(new UserEntity(1,"white"))
                .blackUser(new UserEntity(2, "black"))
                .whiteTime(null)
                .blackTime(null)
                .gameStatus(GameStatus.NOT_STARTED)
                .moveList(moveList)
                .boardStateList(List.of("BoardState#1"))
                .build();
    }
    @Test
    public void gameEntityDTOPlayerMapperTest_WhenGameWhitePlayerIsConverted_ShouldBeSuccesful() {
        ModelMapper mapper = mapperProvider.gameEntityDTOMapper(GameWPlDTO.class);

        GameWPlDTO gameWPlDTO = mapper.map(game, GameWPlDTO.class);

        assertEquals(gameWPlDTO.getGameId(), game.getGameId());
        assertEquals(gameWPlDTO.getWPlDTO().getUsername(), game.getWhiteUser().getUsername());
        assertEquals(gameWPlDTO.getBoardStateList(), game.getBoardStateList());
    }
    @Test
    public void gameEntityDTOBlackPlayerMapperTest_WhenGameBlackPlayerIsConverted_ShouldBeSuccesful() {
        ModelMapper mapper = mapperProvider.gameEntityDTOMapper(GameBPlDTO.class);

        GameBPlDTO gameBPlDTO = mapper.map(game, GameBPlDTO.class);

        assertEquals(gameBPlDTO.getGameId(), game.getGameId());
        assertEquals(gameBPlDTO.getBPlDTO().getUsername(), game.getBlackUser().getUsername());
        assertEquals(gameBPlDTO.getBoardStateList(), game.getBoardStateList());
    }
    @Test
    public void gameEntityDTOBlackPlayerMapperTest_WhenNotSupportedClassIsConverted_ShouldThrowIncorrectConvertTypeException() {
        assertThrows(IncorrectConvertTypeException.class, () -> {
            mapperProvider.gameEntityDTOMapper(GameStateDTO.class);
        });
    }
    //next
    @Test
    public void gameDTOEntityPlayerMapperTest_WhenGameWPlDTOIsConverted_ShouldBeSuccesful() {
        ModelMapper mapper = mapperProvider.gameDTOEntityMapper(GameWPlDTO.class);
        GameWPlDTO gameWPlDTO = new GameWPlDTO(1, new ArrayList<>(), null, new UserDTO("Maciej"));

        GameEntity gameEntity = mapper.map(gameWPlDTO, GameEntity.class);

        assertEquals(gameWPlDTO.getGameId(), gameEntity.getGameId());
        assertEquals(gameWPlDTO.getWPlDTO().getUsername(), gameEntity.getWhiteUser().getUsername());
        assertEquals(gameWPlDTO.getBoardStateList(), gameEntity.getBoardStateList());
    }
    @Test
    public void gameDTOEntityBlackPlayerMapperTest_WhenGameBlackPlayerIsConverted_ShouldBeSuccesful() {
        ModelMapper mapper = mapperProvider.gameDTOEntityMapper(GameBPlDTO.class);
        GameBPlDTO gameBPlDTO = new GameBPlDTO(1, new ArrayList<>(), null, new UserDTO("Maciej"));

        GameEntity gameEntity = mapper.map(gameBPlDTO, GameEntity.class);

        assertEquals(gameBPlDTO.getGameId(), gameEntity.getGameId());
        assertEquals(gameBPlDTO.getBPlDTO().getUsername(), gameEntity.getBlackUser().getUsername());
        assertEquals(gameBPlDTO.getBoardStateList(), gameEntity.getBoardStateList());
    }
    @Test
    public void gameDTOEntityBlackPlayerMapperTest_WhenNotSupportedClassIsConverted_ShouldThrowIncorrectConvertTypeException() {
        assertThrows(IncorrectConvertTypeException.class, () -> {
            mapperProvider.gameDTOEntityMapper(GameStateDTO.class);
        });
    }

}