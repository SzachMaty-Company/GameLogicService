package com.szachmaty.gamelogicservice.application.game;

import com.github.bhlangonijr.chesslib.Side;
import lombok.*;

import java.util.LinkedList;

/**
 * For storing information about current processing move
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameProcessDTO {
    private String gameCode;
    private String move;
    private String currBoardState;
    private String afterMoveBoardState;
    private Side side;
    private LinkedList<Long> gameHistory;
    private Long whiteTime;
    private Long blackTime;
    private Long prevMoveTime;
    private boolean isFirstMove;
    private boolean isFinished;
}
