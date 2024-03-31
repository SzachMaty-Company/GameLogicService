package com.szachmaty.gamelogicservice.data.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import static com.szachmaty.gamelogicservice.data.entity.EntityConstants.GAME_HASH;

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
    @Indexed
    private String whiteUserId;
    @Indexed
    private String blackUserId;
    @ToString.Exclude
    private Long whiteTime;
    @ToString.Exclude
    private Long blackTime;
    private Long prevMoveTime;
    private GameStatus gameStatus;
    private List<String> boardStateList;
    private LinkedList<Long> gameHistory;
    private List<String> moveList;
    private String winner;
    private boolean isGameWithAI;
}
