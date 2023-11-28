package com.szachmaty.gamelogicservice.infrastructure.persistence.repository;

import com.szachmaty.gamelogicservice.infrastructure.persistence.entity.GameEntity;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface GameEntityQuery extends QueryByExampleExecutor<GameEntity> {
}
