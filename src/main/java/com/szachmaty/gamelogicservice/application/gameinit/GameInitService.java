package com.szachmaty.gamelogicservice.application.gameinit;

import com.szachmaty.gamelogicservice.domain.dto.GameDTO;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameInitReq;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameInitResp;

import java.util.List;

public interface GameInitService {
    GameInitResp initGame(GameInitReq gCR);

    List<GameDTO> getAllGames();
}
