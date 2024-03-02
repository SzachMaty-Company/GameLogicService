package com.szachmaty.gamelogicservice.application.game;


import java.util.LinkedList;

public interface MoveProcessor {
    boolean doMove(GameProcessDTO gameProcessDTO);
    LinkedList<Long> getHistory();
    String getBoardState();
}
