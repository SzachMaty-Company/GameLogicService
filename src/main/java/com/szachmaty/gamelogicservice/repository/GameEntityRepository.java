package com.szachmaty.gamelogicservice.repository;

import com.szachmaty.gamelogicservice.data.entity.GameEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameEntityRepository extends CrudRepository<GameEntity, String> {
    GameEntity findByGameCode(String gameCode);
    void delete(GameEntity gameEntity);
}
