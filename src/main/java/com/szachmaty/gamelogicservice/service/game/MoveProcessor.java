package com.szachmaty.gamelogicservice.service.game;


import com.szachmaty.gamelogicservice.data.dto.GameProcessDTO;

import java.util.LinkedList;

public interface MoveProcessor {
    boolean doMove(GameProcessDTO gameProcessDTO);
    LinkedList<Long> getHistory();
    String getBoardState();
}
