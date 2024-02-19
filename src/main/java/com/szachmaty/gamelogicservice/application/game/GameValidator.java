package com.szachmaty.gamelogicservice.application.game;

public interface GameValidator {
    boolean isGameValid(String currBoard, String nextBoardState);
}
