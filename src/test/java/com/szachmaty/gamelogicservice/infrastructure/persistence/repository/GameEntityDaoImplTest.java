package com.szachmaty.gamelogicservice.infrastructure.persistence.repository;

import com.szachmaty.gamelogicservice.infrastructure.persistence.entity.GameEntity;
import com.szachmaty.gamelogicservice.infrastructure.persistence.entity.UserEntity;
import com.szachmaty.gamelogicservice.infrastructure.persistence.entity.enumeration.GameStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class GameEntityDaoImplTest {
    @Autowired
    public GameEntityDaoImpl gameEntityDao;
    private GameEntity gameEntity;

    @BeforeEach
    public void setup() {
        gameEntity = GameEntity.builder()
                .gameId(1)
                .whiteUser(new UserEntity(1,"white"))
                .blackUser(new UserEntity(2, "black"))
                .whiteTime(null)
                .blackTime(null)
                .gameStatus(GameStatus.NOT_STARTED)
                .moveList(new ArrayList<>())
                .build();
    }
    @Test
    public void saveGame_WhenGameEntityIsValid_ShouldReturnGameEntity() {
        gameEntityDao.saveGame(gameEntity);

//        GameEntity retrievedGame = gameEntityDao.findGameById(1);
//
//        System.out.println(retrievedGame.toString());
//        assertNotNull(retrievedGame);
//        assertEquals(retrievedGame, gameEntity);
    }

}