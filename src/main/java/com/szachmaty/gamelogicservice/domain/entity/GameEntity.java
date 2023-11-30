package com.szachmaty.gamelogicservice.domain.entity;

import com.szachmaty.gamelogicservice.domain.entity.enumeration.GameStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static com.szachmaty.gamelogicservice.domain.constants.EntityConstants.GAME_HASH;

@RedisHash(GAME_HASH)
@TypeAlias("GameEntity.class")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private List<String> boardStateList;
    private List<MoveEntity> moveList;

}
