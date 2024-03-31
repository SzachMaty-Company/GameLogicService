package com.szachmaty.gamelogicservice.service.gameinit;

import com.szachmaty.gamelogicservice.data.dto.GameDTO;
import com.szachmaty.gamelogicservice.data.dto.GameInitRequest;
import com.szachmaty.gamelogicservice.data.dto.GameInitResponse;

import java.util.List;

public interface GameInitService {
    GameInitResponse initGame(GameInitRequest gCR);
    List<GameDTO> getAllGames();
}
