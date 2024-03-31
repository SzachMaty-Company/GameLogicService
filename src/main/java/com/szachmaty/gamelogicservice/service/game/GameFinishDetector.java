package com.szachmaty.gamelogicservice.service.game;

import com.szachmaty.gamelogicservice.data.dto.GameProcessDTO;
import com.szachmaty.gamelogicservice.data.dto.GameFinishDTO;


public interface GameFinishDetector {
    GameFinishDTO checkResultBasedOnBoard(GameProcessDTO gameProcessDTO);
    GameFinishDTO checkResultBasedOnTime(GameProcessDTO gameProcessDTO);
}
