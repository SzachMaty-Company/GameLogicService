package com.szachmaty.gamelogicservice.service.game;

import com.szachmaty.gamelogicservice.data.dto.MoveResponseDTO;
import com.szachmaty.gamelogicservice.data.dto.GameMessage;

public interface GameProcessService {
    MoveResponseDTO processGame(GameMessage message);
    MoveResponseDTO processMove(GameMessage message);
}
