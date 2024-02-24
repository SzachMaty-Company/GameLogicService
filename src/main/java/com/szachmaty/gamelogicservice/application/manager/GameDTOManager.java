package com.szachmaty.gamelogicservice.application.manager;

import com.szachmaty.gamelogicservice.domain.dto.GameBPlDTO;
import com.szachmaty.gamelogicservice.domain.dto.GameDTO;
import com.szachmaty.gamelogicservice.domain.dto.GameWPlDTO;
import com.szachmaty.gamelogicservice.domain.entity.GameEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GameDTOManager {
    GameWPlDTO getGameStateWPlById(long gameId);
    GameBPlDTO getGameStateBPlById(long gameId);
    void saveGameStateWPl(GameWPlDTO gameWPlDTO);
    void saveGameStateBPl(GameBPlDTO gameBPlDTO);
    List<String> getBoards(String gameCode);
    void updateBoard(String move, String boardState, String gameCode);
    void saveNewGame(GameDTO gameDTO);
    void deleteGame(GameDTO gameDTO);

}
