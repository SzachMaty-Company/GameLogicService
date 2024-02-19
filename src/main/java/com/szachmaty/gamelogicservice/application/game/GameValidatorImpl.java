package com.szachmaty.gamelogicservice.application.game;

import com.github.bhlangonijr.chesslib.Board;

public class GameValidatorImpl implements GameValidator {
    @Override
    public boolean isGameValid(String currBoardState, String nextBoardState) {
        Board currBoard = new Board();
        currBoard.loadFromFen(currBoardState);
        Board nextBoard = new Board();
        nextBoard.loadFromFen(nextBoardState);
        return true;
    }
}
