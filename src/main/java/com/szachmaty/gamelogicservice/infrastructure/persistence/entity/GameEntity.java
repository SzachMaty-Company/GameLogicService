package com.szachmaty.gamelogicservice.infrastructure.persistence.entity;

import com.szachmaty.gamelogicservice.infrastructure.persistence.entity.enumeration.GameStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.util.List;

@RedisHash("Game")
@Getter
@Setter
@Builder
public class GameEntity {
    @Id
    private long gameId;
    private UserEntity whiteUser;
    private UserEntity blackUser;
    private LocalDateTime whiteTime;
    private LocalDateTime blackTime;
    private GameStatus gameStatus;
    private List<MoveEntity> moveList;

}
