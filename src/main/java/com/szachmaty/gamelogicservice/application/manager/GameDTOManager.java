package com.szachmaty.gamelogicservice.application.manager;

import com.szachmaty.gamelogicservice.application.game.GameProcessDTO;
import com.szachmaty.gamelogicservice.domain.dto.GameDTO;
import com.szachmaty.gamelogicservice.domain.dto.UserDTO;

import java.util.LinkedList;
import java.util.List;

public interface GameDTOManager { //to change class name
    GameDTO getBoards(String gameCode);
    UserDTO getUserById(String userId);
    void saveNewGame(GameDTO gameDTO);
    GameDTO updateBoard(GameProcessDTO gameProcessDTO);
    void deleteGame(GameDTO gameDTO);

    boolean isPlayerGameParticipant(String gameCode, String userId);

    List<GameDTO> getAll();
}
