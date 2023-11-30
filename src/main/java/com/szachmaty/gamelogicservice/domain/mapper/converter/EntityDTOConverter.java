package com.szachmaty.gamelogicservice.domain.mapper.converter;

import com.szachmaty.gamelogicservice.application.manager.GameDTOManager;
import com.szachmaty.gamelogicservice.domain.dto.GameBlackPlayerDTO;
import com.szachmaty.gamelogicservice.domain.dto.GameWhitePlayerDTO;

public class EntityDTOConverter implements GameDTOManager {
    @Override
    public GameWhitePlayerDTO getGameStateForWhitePlayerById(long gameId) {
        return null;
    }

    @Override
    public GameBlackPlayerDTO getGameStateForBlackPlayerById(long gameId) {
        return null;
    }
}
