package com.szachmaty.gamelogicservice.domain.repository;

import com.szachmaty.gamelogicservice.domain.dto.GameStateDTO;
import com.szachmaty.gamelogicservice.domain.entity.GameEntity;

import java.util.List;
import java.util.Optional;

public interface GameEntityDao {

    GameEntity findGameById(long gameId);
    List<GameEntity> findAllGames();
    GameEntity saveGame(GameEntity game);

    GameEntity updateGame(GameEntity game);

    void deleteGame(GameEntity game);

    void deleteGameById(long gameId);
}
