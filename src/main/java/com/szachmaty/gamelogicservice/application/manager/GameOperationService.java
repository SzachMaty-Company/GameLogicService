package com.szachmaty.gamelogicservice.application.manager;

import com.szachmaty.gamelogicservice.application.game.GameProcessDTO;
import com.szachmaty.gamelogicservice.domain.dto.GameDTO;
import com.szachmaty.gamelogicservice.domain.dto.UserDTO;

import java.util.List;

public interface GameOperationService {
    GameDTO getBoards(String gameCode);
    void saveNewGame(GameDTO gameDTO);
    GameDTO updateBoard(GameProcessDTO gameProcessDTO);
    void deleteGameByGameCode(String gameCode);
    boolean isPlayerGameParticipant(String gameCode, String userId);
    List<GameDTO> getAll();
}
