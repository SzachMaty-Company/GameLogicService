package com.szachmaty.gamelogicservice.domain.repository;

import com.szachmaty.gamelogicservice.domain.entity.GameEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameEntityRepository extends CrudRepository<GameEntity, String> {
    GameEntity findByGameCode(String gameCode);
    GameEntity findByBlackUserUuid(String uuid);
    GameEntity findByWhiteUserUuid(String uuid);
}
