package com.szachmaty.gamelogicservice.service.game.chain;

import com.szachmaty.gamelogicservice.data.dto.GameProcessContext;

public interface GameChainFactory {
    GameChainList createChainForGame(GameProcessContext context);
}
