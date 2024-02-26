package com.szachmaty.gamelogicservice.application.game;

import com.github.bhlangonijr.chesslib.Side;

import java.util.LinkedList;
import java.util.List;

public interface MoveProcessor {
    boolean doMove(String currMove, String currBoardState, Side side);
    LinkedList<Long> getHistory();
    String getBoardState();
}
