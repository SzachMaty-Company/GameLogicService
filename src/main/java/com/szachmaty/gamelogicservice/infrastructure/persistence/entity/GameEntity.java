package com.szachmaty.gamelogicservice.infrastructure.persistence.entity;

import com.szachmaty.gamelogicservice.infrastructure.persistence.entity.enumeration.GameStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@RedisHash("game")
@TypeAlias("GameEntity.class")
@Getter
@Setter
@Builder
@ToString
public class GameEntity implements Serializable {
    @Id
    private long gameId;
    private UserEntity whiteUser;
    private UserEntity blackUser;
    @ToString.Exclude
    private LocalDateTime whiteTime;
    @ToString.Exclude
    private LocalDateTime blackTime;
    private GameStatus gameStatus;
    private String currentBoard;
    private List<MoveEntity> moveList;

}
