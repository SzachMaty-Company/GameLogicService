package com.szachmaty.gamelogicservice.application.game;

import com.github.bhlangonijr.chesslib.Side;
import com.szachmaty.gamelogicservice.domain.dto.GameDTO;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameFinishDTO;

public interface GameFinishDetector {
    GameFinishDTO checkResultBasedOnBoard(String boardState, Side side);
    GameFinishDTO checkResultBasedOnTime(GameDTO gameDTO, String boardState);
}
