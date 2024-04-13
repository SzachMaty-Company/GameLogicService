package com.szachmaty.gamelogicservice.service.game.chain;

import com.szachmaty.gamelogicservice.data.dto.GameProcessContext;

public interface GameProcessHandler {
    boolean process(GameProcessContext context);
}
