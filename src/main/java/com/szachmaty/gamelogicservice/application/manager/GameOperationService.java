package com.szachmaty.gamelogicservice.application.manager;

import com.szachmaty.gamelogicservice.application.game.GameProcessDTO;
import com.szachmaty.gamelogicservice.domain.dto.GameDTO;
import com.szachmaty.gamelogicservice.domain.dto.UserDTO;

import java.util.List;

public interface GameOperationService { //to change class name
    GameDTO getBoards(String gameCode);
    UserDTO getUserById(String userId);
    void saveNewGame(GameDTO gameDTO);
    GameDTO updateBoard(GameProcessDTO gameProcessDTO);
    void deleteGame(GameDTO gameDTO);
    void deleteGameByGameCode(String gameCode);
    boolean isPlayerGameParticipant(String gameCode, String userId);
    List<GameDTO> getAll();
}
