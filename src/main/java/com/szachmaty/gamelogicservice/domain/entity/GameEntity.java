package com.szachmaty.gamelogicservice.domain.entity;

import com.szachmaty.gamelogicservice.domain.entity.enumeration.GameStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

import static com.szachmaty.gamelogicservice.domain.entity.constants.EntityConstants.GAME_HASH;

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
    private String gameId;
    @Indexed
    private String gameCode;
    private UserEntity whiteUser;
    private UserEntity blackUser;
    @ToString.Exclude
    private Long whiteTime;
    @ToString.Exclude
    private Long blackTime;
    private Long prevMoveTime;
    private GameStatus gameStatus;
    private List<String> boardStateList;
    private LinkedList<Long> gameHistory;
    private List<MoveEntity> moveList;
    private String winner;
}
