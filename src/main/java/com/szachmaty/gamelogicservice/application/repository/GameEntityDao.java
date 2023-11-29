package com.szachmaty.gamelogicservice.application.repository;

import com.szachmaty.gamelogicservice.domain.entity.GameEntity;

import java.util.List;

public interface GameEntityDao {

    GameEntity findGameById(long gameId);
    GameEntity findGameSubsetByWhiteUser(long gameId);
    GameEntity findGameSubsetByBlackUser(long gameId);
    List<GameEntity> findAllGames();
    GameEntity saveGame(GameEntity game);

    GameEntity updateGame(GameEntity game);

    GameEntity deleteGame(GameEntity game);

    void deleteGameById(long gameId);
}
