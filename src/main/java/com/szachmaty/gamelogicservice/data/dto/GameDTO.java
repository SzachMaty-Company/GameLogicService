package com.szachmaty.gamelogicservice.data.dto;

import com.github.bhlangonijr.chesslib.Side;
import com.szachmaty.gamelogicservice.data.entity.GameStatus;
import lombok.*;

import java.util.LinkedList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameDTO {
    private String gameCode;
    private String whiteUserId;
    private String blackUserId;
    private Long whiteTime;
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
