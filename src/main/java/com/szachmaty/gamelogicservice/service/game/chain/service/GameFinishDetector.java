package com.szachmaty.gamelogicservice.service.game.chain.service;

import com.szachmaty.gamelogicservice.data.dto.GameProcessContext;
import com.szachmaty.gamelogicservice.data.entity.GameStatus;


public interface GameFinishDetector {
    GameStatus checkResultBasedOnBoard(GameProcessContext gameProcessContext);
    GameStatus checkResultBasedOnTime(GameProcessContext gameProcessContext);
}
