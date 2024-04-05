package com.szachmaty.gamelogicservice.repository;

import com.szachmaty.gamelogicservice.data.entity.GameEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameEntityRepository extends CrudRepository<GameEntity, String> {
    GameEntity findByGameCode(String gameCode);
    GameEntity findFirstByBlackUserId(String userId);
    GameEntity findFirstByWhiteUserId(String userId);
    void delete(GameEntity gameEntity);
}
