package com.szachmaty.gamelogicservice.application.game;

import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameFinishDTO;


public interface GameFinishDetector {
    GameFinishDTO checkResultBasedOnBoard(GameProcessDTO gameProcessDTO);
    GameFinishDTO checkResultBasedOnTime(GameProcessDTO gameProcessDTO);
}
