package com.szachmaty.gamelogicservice.repository;

import com.szachmaty.gamelogicservice.data.dto.GameProcessContext;
import com.szachmaty.gamelogicservice.data.dto.GameDTO;

import java.util.List;

public interface GameOperationService {
    GameDTO getGameByGameCode(String gameCode);
    void saveNewGame(GameDTO gameDTO);
    GameDTO updateBoard(GameProcessContext gameProcessContext);
    void deleteGameByGameCode(String gameCode);
    boolean isPlayerGameParticipant(String gameCode, String userId);
    List<GameDTO> getAll();
    boolean validatePlayerTurn(String gameCode, String principal);
}
