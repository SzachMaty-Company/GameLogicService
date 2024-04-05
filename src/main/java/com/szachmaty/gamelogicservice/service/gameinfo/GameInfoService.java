package com.szachmaty.gamelogicservice.service.gameinfo;

import com.szachmaty.gamelogicservice.data.dto.GameInfoDTO;

public interface GameInfoService {
    GameInfoDTO getGameByGameCode(String gameCode);
}
