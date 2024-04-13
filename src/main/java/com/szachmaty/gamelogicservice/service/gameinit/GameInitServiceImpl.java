package com.szachmaty.gamelogicservice.service.gameinit;

import com.github.bhlangonijr.chesslib.Side;
import com.szachmaty.gamelogicservice.data.dto.*;
import com.szachmaty.gamelogicservice.data.entity.GameStatus;
import com.szachmaty.gamelogicservice.repository.GameOperationService;
import com.szachmaty.gamelogicservice.service.game.external.ChatServiceEventData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameInitServiceImpl implements GameInitService {

    private final GameOperationService gameOperationService;
    private final ApplicationEventPublisher applicationEventPublisher;

    private final static String GAME_INIT_ERROR = "Cannot create valid gameCode!";

    @Override
    public GameInitResponse initGame(GameInitRequest initReq) {
        String gameCode = GameInitUtil.generateGameCode();
        if("".equals(gameCode)) {
            throw new GameInitException(GAME_INIT_ERROR);
        }
        Long playersTime = GameInitUtil.gameTimeParser(initReq.gameTime());
        String gameStartTime = GameInitUtil.gameStartTimeCreator();
        boolean isAIFlow = GameMode.isAIMode(initReq.gameMode());

        boolean isPlayer1White = initReq.player1PieceColor().equals(PlayerColor.WHITE);
        String player1 = initReq.player1();
        String player2 = initReq.player2();

        GameDTO gameDTO = GameDTO.builder()
                .gameCode(gameCode)
                .whiteUserId(isPlayer1White ? player1 : player2)
                .blackUserId(isPlayer1White ? player2 : player1)
                .whiteTime(playersTime)
                .blackTime(playersTime)
                .prevSystemTime(null)
                .sideToMove(Side.WHITE)
                .gameStatus(GameStatus.NOT_STARTED)
                .gameMode(initReq.gameMode())
                .fenList(new ArrayList<>())
                .moveList(new ArrayList<>())
                .gameDuration(playersTime.toString())
                .gameStartTime(gameStartTime)
                .build();

        gameOperationService.saveNewGame(gameDTO);

        if(!isAIFlow) {
            ChatServiceEventData eventData =
                    new ChatServiceEventData(this, gameDTO);
            applicationEventPublisher.publishEvent(eventData); //async
        }

        return new GameInitResponse(gameCode);
    }

    @Override
    public List<GameDTO> getAllGames() {
        return gameOperationService.getAll();
    }
}
