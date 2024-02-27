package com.szachmaty.gamelogicservice.application.manager;

import com.szachmaty.gamelogicservice.domain.dto.GameDTO;
import com.szachmaty.gamelogicservice.domain.dto.UserDTO;

import java.util.LinkedList;

public interface GameDTOManager {
    GameDTO getBoards(String gameCode);
    UserDTO getUserById(String userId);
    void saveNewGame(GameDTO gameDTO);
    GameDTO updateBoard(String move, String boardState, String gameCode, LinkedList<Long> gameHistory, boolean isFinished);
    void deleteGame(GameDTO gameDTO);

    boolean isPlayerGameParticipant(String gameCode, String userId);
}
