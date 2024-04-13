package com.szachmaty.gamelogicservice.service.game.chain;

import com.szachmaty.gamelogicservice.data.dto.GameMessage;
import com.szachmaty.gamelogicservice.data.dto.MoveResponseDTO;

public interface GamePreparation {
    MoveResponseDTO prepare(GameMessage message);
}
