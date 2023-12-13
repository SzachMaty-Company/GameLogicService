package com.szachmaty.gamelogicservice.domain.mapper.converter;

import com.szachmaty.gamelogicservice.application.move.Move;
import com.szachmaty.gamelogicservice.domain.dto.GameWPlDTO;
import com.szachmaty.gamelogicservice.domain.entity.GameEntity;
import com.szachmaty.gamelogicservice.domain.entity.MoveEntity;
import com.szachmaty.gamelogicservice.domain.entity.UserEntity;
import com.szachmaty.gamelogicservice.domain.entity.enumeration.GameStatus;
import com.szachmaty.gamelogicservice.domain.repository.GameEntityDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntityDTOConverterTest {
    @Mock
    private GameEntityDao gameEntityDao;
    @InjectMocks
    private EntityDTOConverter entityDTOConverter;

    private GameEntity game;
    @BeforeEach
    public void setup() {
        List<MoveEntity> moveList = new LinkedList<>();
        moveList.add(new MoveEntity(1, "WA1"));
        game = GameEntity.builder()
                .gameId(1)
                .whiteUser(new UserEntity(1,"white"))
                .blackUser(new UserEntity(2, "black"))
                .whiteTime(null)
                .blackTime(null)
                .gameStatus(GameStatus.NOT_STARTED)
                .moveList(moveList)
                .boardStateList(List.of("BoardState#1"))
                .build();

        when(gameEntityDao.findGameById(1)).thenReturn(game);

    }
    @Test
    public void getGameStateWPlById_WhenBaseTestCase_ShouldBeSuccesful() {
        GameWPlDTO gameWPlDTO = entityDTOConverter.getGameStateWPlById(1);

        assertEquals(gameWPlDTO.getGameId(), game.getGameId());
        assertEquals(gameWPlDTO.getWPlDTO().getUsername(), game.getWhiteUser().getUsername());
        assertEquals(gameWPlDTO.getWhiteTime(), game.getWhiteTime());
        assertEquals(gameWPlDTO.getBoardStateList(), game.getBoardStateList());
    }
}