package com.szachmaty.gamelogicservice.application.manager;

import com.szachmaty.gamelogicservice.domain.dto.GameBPlDTO;
import com.szachmaty.gamelogicservice.domain.dto.GameDTO;
import com.szachmaty.gamelogicservice.domain.dto.GameWPlDTO;
import com.szachmaty.gamelogicservice.domain.dto.UserDTO;

import java.util.LinkedList;
import java.util.List;

public interface GameDTOManager {
    GameWPlDTO getGameStateWPlById(long gameId);
    GameBPlDTO getGameStateBPlById(long gameId);
    void saveGameStateWPl(GameWPlDTO gameWPlDTO);
    void saveGameStateBPl(GameBPlDTO gameBPlDTO);
    GameDTO getBoards(String gameCode);
    UserDTO getUserById(String userId);
    GameDTO updateBoard(String move, String boardState, String gameCode, LinkedList<Long> gameHistory, boolean isFinished);
    void saveNewGame(GameDTO gameDTO);
    void deleteGame(GameDTO gameDTO);
    boolean isPlayerGameParticipant(String gameCode, String userId);
}
