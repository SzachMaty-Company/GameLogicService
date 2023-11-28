package com.szachmaty.gamelogicservice.infrastructure.persistence.repository;

import com.szachmaty.gamelogicservice.infrastructure.persistence.entity.GameEntity;

import java.util.List;

public interface GameEntityDao {

    GameEntity findGameById(long gameId);

    List<GameEntity> findAllGames();
    GameEntity saveGame(GameEntity game);

    GameEntity updateGame(GameEntity game);

    GameEntity deleteGame(GameEntity game);

    void deleteGameById(long gameId);
}
