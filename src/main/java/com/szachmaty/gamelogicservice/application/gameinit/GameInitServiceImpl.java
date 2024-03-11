package com.szachmaty.gamelogicservice.application.gameinit;

import com.github.bhlangonijr.chesslib.game.Game;
import com.szachmaty.gamelogicservice.application.manager.GameOperationService;
import com.szachmaty.gamelogicservice.domain.dto.GameDTO;
import com.szachmaty.gamelogicservice.domain.entity.GameStatus;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameInitServiceImpl implements GameInitService {

    private final GameOperationService gameOperationService;
    private final static String AI = "AI";
    private final static String FRIEND = "FRIEND";


    public GameInitRes initGame(GameInitReq initReq) {
        if(initReq.gameMode().equals(AI)) {
            return createGameAIvsUser(initReq);
        } else if(initReq.gameMode().equals(FRIEND)) {
            return createGameUservsUser(initReq);
        } else {
            throw new GameInitException("Incorrect gameMode - " + initReq.gameMode());
        }
    }

    @Override
    public List<GameDTO> getAllGames() {
        return gameOperationService.getAll();
    }

    private GameInitRes createGameAIvsUser(GameInitReq initReq) {
        return null; //TO DO
    }

    private GameInitRes createGameUservsUser(GameInitReq initReq) {
        String gameCode = GameInitUtil.generateGameCode();
        if("".equals(gameCode)) {
            throw new GameInitException("Cannot create valid gameCode!");
        }
        Long parsedTime = GameInitUtil.gameTimeParser(initReq.gameTime());

        GameDTO gameDTO = GameDTO.builder()
                .gameCode(gameCode)
                .whiteUserId(initReq.player1())
                .blackUserId(initReq.player2())
                .gameStatus(GameStatus.NOT_STARTED)
                .blackTime(parsedTime)
                .whiteTime(parsedTime)
                .prevMoveTime(parsedTime*3)
                .boardStateList(new ArrayList<>())
                .moveList(new ArrayList<>())
                .build();

        gameOperationService.saveNewGame(gameDTO);
        return new GameInitRes(gameCode);
    }
}
