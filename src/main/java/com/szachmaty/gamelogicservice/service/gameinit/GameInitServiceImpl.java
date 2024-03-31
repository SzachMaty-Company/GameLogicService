package com.szachmaty.gamelogicservice.service.gameinit;

import com.szachmaty.gamelogicservice.repository.GameOperationService;
import com.szachmaty.gamelogicservice.data.dto.GameDTO;
import com.szachmaty.gamelogicservice.data.entity.GameStatus;
import com.szachmaty.gamelogicservice.data.dto.GameInitRequest;
import com.szachmaty.gamelogicservice.data.dto.GameInitResponse;
import com.szachmaty.gamelogicservice.data.dto.GameMode;
import com.szachmaty.gamelogicservice.data.dto.PlayerColor;
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

    public GameInitResponse initGame(GameInitRequest initReq) {
        return createGame(initReq);
    }

    @Override
    public List<GameDTO> getAllGames() {
        return gameOperationService.getAll();
    }


    private GameInitResponse createGame(GameInitRequest initReq) {
        String gameCode = GameInitUtil.generateGameCode();
        if("".equals(gameCode)) {
            throw new GameInitException("Cannot create valid gameCode!");
        }
        Long parsedTime = GameInitUtil.gameTimeParser(initReq.gameTime());

        boolean isAIMode = initReq.gameMode().equals(GameMode.AI);
        boolean isPlayer1White = initReq.player1PieceColor().equals(PlayerColor.WHITE);
        String player1 = initReq.player1();
        String player2 = initReq.player2();

//        handle AI playing white
        GameDTO gameDTO = GameDTO.builder()
                .gameCode(gameCode)
                .whiteUserId(isPlayer1White ? player1 : player2)
                .blackUserId(isPlayer1White ? player2 : player1)
                .gameStatus(GameStatus.NOT_STARTED)
                .blackTime(parsedTime)
                .whiteTime(parsedTime)
                .prevMoveTime(parsedTime*3)
                .boardStateList(new ArrayList<>())
                .moveList(new ArrayList<>())
                .isGameWithAI(isAIMode)
                .build();

        gameOperationService.saveNewGame(gameDTO);
        return new GameInitResponse(gameCode);
    }
}
