package com.szachmaty.gamelogicservice.application.gameinit;

import com.szachmaty.gamelogicservice.domain.dto.GameWPlDTO;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameInitReq;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameInitResp;

public interface GameInitService {
    GameInitResp initGame(GameInitReq gCR);

    GameWPlDTO getGame();
}
