package com.szachmaty.gamelogicservice.data.dto;

import com.github.bhlangonijr.chesslib.Side;
import com.szachmaty.gamelogicservice.data.entity.GameStatus;
import lombok.*;

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GameDTO {
    private String gameCode;
    private String whiteUserId;
    private String blackUserId;
    private Long whiteTime;
    private Long blackTime;
    private Long prevSystemTime;
    private GameStatus gameStatus;
    private Side sideToMove;
    private List<String> boardStateList;
    private LinkedList<Long> gameHistory;
    private List<String> moveList;
    private boolean isGameWithAI;
}
