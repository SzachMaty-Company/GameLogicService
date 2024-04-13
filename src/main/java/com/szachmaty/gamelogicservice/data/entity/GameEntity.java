package com.szachmaty.gamelogicservice.data.entity;

import com.github.bhlangonijr.chesslib.Side;
import com.szachmaty.gamelogicservice.data.dto.GameMode;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private Long prevSystemTime;
    private Side sideToMove;
    private GameStatus gameStatus;
    private GameMode gameMode;
    private List<String> fenList;
    private LinkedList<Long> gameHistory;
    private List<String> moveList;
    private String gameDuration;
    private String gameStartTime;
}
