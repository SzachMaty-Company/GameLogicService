package com.szachmaty.gamelogicservice.application.game;

import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameMoveInfoMessage;

public interface GameProcessService {
    String process(String move, String gameCode);
}
