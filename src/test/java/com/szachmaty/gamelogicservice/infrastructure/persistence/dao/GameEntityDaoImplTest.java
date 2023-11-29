package com.szachmaty.gamelogicservice.infrastructure.persistence.dao;

import com.szachmaty.gamelogicservice.domain.entity.GameEntity;
import com.szachmaty.gamelogicservice.domain.entity.UserEntity;
import com.szachmaty.gamelogicservice.domain.entity.enumeration.GameStatus;
import com.szachmaty.gamelogicservice.application.repository.GameEntityDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class GameEntityDaoImplTest {
    @Autowired
    public GameEntityDao gameEntityDao;
    private GameEntity gameEntity;

    @Autowired
    private RedisTemplate<String, Object> template;

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
                .boardList(new ArrayList<>())
                .build();
    }
    @Test
    public void getGameTest() {
        gameEntityDao.saveGame(gameEntity);

        GameEntity retrivedGame = gameEntityDao.findGameById(1);

        assertEquals(1,retrivedGame.getGameId());
        assertEquals(1,retrivedGame.getWhiteUser().getUserId());
    }
    @Test
    public void getNullGameTest() {
        GameEntity retrivedGame = gameEntityDao.findGameById(110230123);

        assertNull(retrivedGame);
    }
    @Test
    public void saveGameTest() {
        gameEntityDao.saveGame(gameEntity);

        GameEntity retrievedGameEntity = gameEntityDao.findGameById(gameEntity.getGameId());
        assertEquals(gameEntity.getGameId(),retrievedGameEntity.getGameId());
    }

    @Test
    public void updateGameTest() {
        GameEntity toUpdatedGE = prepareUpdatedGameEntity();
        gameEntityDao.saveGame(gameEntity);

        gameEntityDao.updateGame(toUpdatedGE);
        GameEntity retrievedGE = gameEntityDao.findGameById(gameEntity.getGameId());

        assertNotEquals(retrievedGE.getGameStatus(), gameEntity.getGameStatus());
    }
    @Test
    public void deleteGameByIdTest() {
        gameEntityDao.saveGame(gameEntity);

        gameEntityDao.deleteGameById(gameEntity.getGameId());

        GameEntity retrievedGE = gameEntityDao.findGameById(gameEntity.getGameId());

        assertNull(retrievedGE);
    }

    private GameEntity prepareUpdatedGameEntity() {
        GameEntity updatedGameEntity = GameEntity.builder()
                .gameId(1)
                .whiteUser(new UserEntity(1,"white"))
                .blackUser(new UserEntity(2, "black"))
                .whiteTime(null)
                .blackTime(null)
                .gameStatus(GameStatus.FINISHED)
                .moveList(new ArrayList<>())
                .build();
        return updatedGameEntity;
    }
}