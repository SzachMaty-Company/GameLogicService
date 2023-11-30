package com.szachmaty.gamelogicservice.application.manager;

import com.szachmaty.gamelogicservice.domain.dto.GameBlackPlayerDTO;
import com.szachmaty.gamelogicservice.domain.dto.GameWhitePlayerDTO;

public interface GameDTOManager {
    GameWhitePlayerDTO getGameStateForWhitePlayerById(long gameId);
    GameBlackPlayerDTO getGameStateForBlackPlayerById(long gameId);

}
