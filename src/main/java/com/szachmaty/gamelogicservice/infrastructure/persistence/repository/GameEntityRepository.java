package com.szachmaty.gamelogicservice.infrastructure.persistence.repository;

import com.szachmaty.gamelogicservice.infrastructure.persistence.entity.GameEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameEntityRepository extends CrudRepository<GameEntity, Long> {
}
