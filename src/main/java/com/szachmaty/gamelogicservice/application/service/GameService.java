package com.szachmaty.gamelogicservice.application.service;

import com.szachmaty.gamelogicservice.application.manager.GameDTOManager;
import com.szachmaty.gamelogicservice.application.util.Util;
import com.szachmaty.gamelogicservice.domain.dto.GameDTO;
import com.szachmaty.gamelogicservice.domain.dto.GameWPlDTO;
import com.szachmaty.gamelogicservice.domain.dto.UserDTO;
import com.szachmaty.gamelogicservice.domain.entity.enumeration.GameStatus;
import com.szachmaty.gamelogicservice.infrastructure.controller.apiclient.GameClient;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameCheckPlayerReq;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
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

    public String createGame(GameCreateRequest gCR) {
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
//        gameClient.getTestEntries();
//        try {
        try {
            gameClient.checkIfPlayerExists(gameCheckPlayerReq);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage());
//        }

        return "HA_HA";
    }
}
