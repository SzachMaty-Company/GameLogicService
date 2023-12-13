package com.szachmaty.gamelogicservice.application.manager;

import com.szachmaty.gamelogicservice.domain.dto.GameBPlDTO;
import com.szachmaty.gamelogicservice.domain.dto.GameWPlDTO;

public interface GameDTOManager {
    GameWPlDTO getGameStateWPlById(long gameId);
    GameBPlDTO getGameStateBPlById(long gameId);
    void saveGameStateWPl(GameWPlDTO gameWPlDTO);
    void saveGameStateBPl(GameBPlDTO gameBPlDTO);
}
