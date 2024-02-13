package com.szachmaty.gamelogicservice.application.service;

import com.szachmaty.gamelogicservice.application.manager.GameDTOManager;
import com.szachmaty.gamelogicservice.application.util.Util;
import com.szachmaty.gamelogicservice.domain.dto.GameDTO;
import com.szachmaty.gamelogicservice.domain.dto.GameWPlDTO;
import com.szachmaty.gamelogicservice.domain.dto.UserDTO;
import com.szachmaty.gamelogicservice.domain.entity.enumeration.GameStatus;
import com.szachmaty.gamelogicservice.infrastructure.controller.apiclient.GameClient;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.CheckPlayerResp;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameCheckPlayerReq;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameCreateReq;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameInitResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {
    private final GameDTOManager gameDTOManager;
    private final GameClient gameClient;
    public String getGame() {
        GameWPlDTO bialas = gameDTOManager.getGameStateWPlById(1);
        log.info(bialas.toString());
        return "działą";
    }

    public GameInitResp createGame(GameCreateReq gCR) {
        String gameCode = Util.generateGameCode();
        LocalTime parsedTime = Util.gameTimeParser(gCR.gameTime());

        UserDTO whitePlayer = UserDTO.builder()
                .username(gCR.player1())
                .build();

        UserDTO blackPlayer = UserDTO.builder()
                .username(gCR.player2())
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
                .oponent(gCR.player2())
                .gameCode(gameCode)
                .build();
        CheckPlayerResp res;
//        gameClient.getTestEntries();
        res = gameClient.checkIfPlayerExists(gameCheckPlayerReq);

        if(res != null && res.isPlayer2Exists()) {
            return new GameInitResp(gameCode);
        } else {
            gameDTOManager.deleteGame(gameDTO);
            throw new RuntimeException("Not exists"); //TO BE CHANGED
        }
    }
}
