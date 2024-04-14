package com.szachmaty.gamelogicservice.data.dto;

import com.github.bhlangonijr.chesslib.Side;
import com.szachmaty.gamelogicservice.data.entity.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;

/**
 * For storing information about current processing move
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameProcessContext {
    private String gameCode;
    private String move;
    private String currFen;
    private String nextFen;
    private Side side;
    private LinkedList<Long> gameHistory;
    private Long whiteTime;
    private Long blackTime;
    private Long prevSystemTime;
    private GameStatus gameStatus;
    private boolean isFirstMove;
}
