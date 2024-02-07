package com.szachmaty.gamelogicservice.application.service;

import com.szachmaty.gamelogicservice.application.manager.GameDTOManager;
import com.szachmaty.gamelogicservice.domain.dto.GameWPlDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GameService {


    private final GameDTOManager gameDTOManager;

    public GameService(GameDTOManager gameDTOManager) {
        this.gameDTOManager = gameDTOManager;
    }
    public String getGame() {
        GameWPlDTO bialas = gameDTOManager.getGameStateWPlById(1);
        log.info(bialas.toString());
        return "działą";
    }
}
