package com.szachmaty.gamelogicservice.application.gameinit;

import com.szachmaty.gamelogicservice.application.manager.GameDTOManager;
import com.szachmaty.gamelogicservice.domain.dto.GameDTO;
import com.szachmaty.gamelogicservice.domain.dto.GameWPlDTO;
import com.szachmaty.gamelogicservice.domain.dto.UserDTO;
import com.szachmaty.gamelogicservice.domain.entity.enumeration.GameStatus;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameInitServiceImpl implements GameInitService {
    private final GameDTOManager gameDTOManager;
    private final static String AI = "AI";
    private final static String FRIEND = "FRIEND";

    public GameWPlDTO getGame() {
        GameWPlDTO bialas = gameDTOManager.getGameStateWPlById(0);
        log.info(bialas.toString());
        return bialas;
    }

    public GameInitResp initGame(GameInitReq initReq) {
        if(initReq.gameMode().equals(AI)) {
            return createGameAIvsUser(initReq);
        } else if(initReq.gameMode().equals(FRIEND)) {
            return createGameUservsUser(initReq);
        } else {
            throw new GameInitException("Incorrect gameMode - " + initReq.gameMode());
        }
    }

    private GameInitResp createGameAIvsUser(GameInitReq initReq) {
        return null; //TO DO
    }

    private GameInitResp createGameUservsUser(GameInitReq initReq) {
        String gameCode = GameInitUtil.generateGameCode();
        LocalTime parsedTime = GameInitUtil.gameTimeParser(initReq.gameTime());

        UserDTO whitePlayer = UserDTO.builder()
                .username(initReq.player1())
                .build();

        UserDTO blackPlayer = UserDTO.builder()
                .username(initReq.player2())
                .build();

        GameDTO gameDTO = GameDTO.builder()
                .gameCode(gameCode)
                .blackUser(blackPlayer)
                .whiteUser(whitePlayer)
                .gameStatus(GameStatus.NOT_STARTED)
                .blackTime(parsedTime)
                .whiteTime(parsedTime)
                .boardStateList(new ArrayList<>())
                .moveList(new ArrayList<>())
                .build();

        gameDTOManager.saveNewGame(gameDTO);
        GameCheckPlayerReq gameCheckPlayerReq = GameCheckPlayerReq.builder()
                .oponent(initReq.player2())
                .gameCode(gameCode)
                .build();
        CheckPlayerResp res = null;
        try {
//            res = gameClient.checkIfPlayerExists(gameCheckPlayerReq);
            res = new CheckPlayerResp(true); //MOCKED
        } catch(Exception e) {
            gameDTOManager.deleteGame(gameDTO);
            throw e;
        }

        if(res != null && res.isPlayer2Exists()) {
            return new GameInitResp(gameCode);
        } else {
            gameDTOManager.deleteGame(gameDTO);
            throw new GameInitException("Not exists!"); //TO BE CHANGED
        }
    }
}
