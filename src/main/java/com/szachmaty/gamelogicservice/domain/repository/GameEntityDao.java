package com.szachmaty.gamelogicservice.domain.repository;

import com.szachmaty.gamelogicservice.domain.entity.GameEntity;

import java.util.List;

public interface GameEntityDao {

    GameEntity findGameById(long gameId);
    GameEntity findGameByGameCode(String gameCode);
    List<GameEntity> findAllGames();
    GameEntity saveGame(GameEntity game);

    GameEntity updateGame(GameEntity game);

    void deleteGame(GameEntity game);

    void deleteGameById(long gameId);
}
