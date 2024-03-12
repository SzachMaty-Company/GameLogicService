package com.szachmaty.gamelogicservice.application.game;

import com.szachmaty.gamelogicservice.infrastructure.controller.data.MoveResponseDTO;
import com.szachmaty.gamelogicservice.infrastructure.controller.ws.GameMessage;

import java.security.Principal;

public interface GameProcessService {
    MoveResponseDTO process(GameMessage message, Principal principal);
}
