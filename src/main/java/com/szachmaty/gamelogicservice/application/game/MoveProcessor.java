package com.szachmaty.gamelogicservice.application.game;

import com.github.bhlangonijr.chesslib.Side;

import java.util.List;

public interface MoveProcessor {
    String doMove(String currMove, String currBoardState, Side side);
}
