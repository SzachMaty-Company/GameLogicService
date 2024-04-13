package com.szachmaty.gamelogicservice.service.game;


import com.szachmaty.gamelogicservice.data.dto.GameProcessContext;

import java.util.LinkedList;

public interface MoveProcessor {
    boolean doMove(GameProcessContext gameProcessContext);
    LinkedList<Long> getHistory();
    String getBoardState();
}
