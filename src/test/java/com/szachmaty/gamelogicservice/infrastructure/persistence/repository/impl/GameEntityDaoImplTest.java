package com.szachmaty.gamelogicservice.infrastructure.persistence.repository.impl;

import com.szachmaty.gamelogicservice.domain.game.Game;
import com.szachmaty.gamelogicservice.infrastructure.persistence.entity.GameEntity;
import com.szachmaty.gamelogicservice.infrastructure.persistence.entity.UserEntity;
import com.szachmaty.gamelogicservice.infrastructure.persistence.entity.enumeration.GameStatus;
import com.szachmaty.gamelogicservice.infrastructure.persistence.repository.GameEntityDao;
import com.szachmaty.gamelogicservice.infrastructure.persistence.repository.impl.GameEntityDaoImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class GameEntityDaoImplTest {
    @Autowired
    public GameEntityDao gameEntityDao;
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
    public void getGameTest() {
        gameEntityDao.saveGame(gameEntity);

        GameEntity retrivedGame = gameEntityDao.findGameById(1);

        assertEquals(1,retrivedGame.getGameId());
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
    @AfterEach
    public void clearRedisCache() {

    }

}