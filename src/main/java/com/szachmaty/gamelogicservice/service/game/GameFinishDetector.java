package com.szachmaty.gamelogicservice.service.game;

import com.szachmaty.gamelogicservice.data.dto.GameProcessDTO;
import com.szachmaty.gamelogicservice.data.entity.GameStatus;


public interface GameFinishDetector {
    GameStatus checkResultBasedOnBoard(GameProcessDTO gameProcessDTO);
    GameStatus checkResultBasedOnTime(GameProcessDTO gameProcessDTO);
}
