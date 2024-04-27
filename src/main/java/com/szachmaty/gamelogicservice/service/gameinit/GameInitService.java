package com.szachmaty.gamelogicservice.service.gameinit;

import com.szachmaty.gamelogicservice.data.dto.GameInitRequest;
import com.szachmaty.gamelogicservice.data.dto.GameInitResponse;

public interface GameInitService {
    GameInitResponse initGame(GameInitRequest gCR);
}
