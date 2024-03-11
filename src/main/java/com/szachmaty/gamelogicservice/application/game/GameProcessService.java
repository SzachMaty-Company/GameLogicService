package com.szachmaty.gamelogicservice.application.game;

import com.szachmaty.gamelogicservice.infrastructure.controller.data.MoveResponseDTO;
import com.szachmaty.gamelogicservice.infrastructure.controller.ws.GameMessage;

public interface GameProcessService {
    MoveResponseDTO process(GameMessage message);
}
