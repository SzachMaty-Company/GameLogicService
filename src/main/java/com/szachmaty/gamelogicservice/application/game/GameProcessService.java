package com.szachmaty.gamelogicservice.application.game;

import com.szachmaty.gamelogicservice.infrastructure.controller.ws.GameMessage;

public interface GameProcessService {
    String process(GameMessage message);
}
