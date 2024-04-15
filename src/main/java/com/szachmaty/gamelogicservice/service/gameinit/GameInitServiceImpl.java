package com.szachmaty.gamelogicservice.service.gameinit;

import com.github.bhlangonijr.chesslib.Side;
import com.szachmaty.gamelogicservice.data.dto.*;
import com.szachmaty.gamelogicservice.data.entity.GameStatus;
import com.szachmaty.gamelogicservice.repository.GameOperationService;
import com.szachmaty.gamelogicservice.service.event.AIMessageEventData;
import com.szachmaty.gamelogicservice.service.event.ChatServiceEventData;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameInitServiceImpl implements GameInitService {

    private final GameOperationService gameOperationService;
    private final GameInitNotificationConverter gameInitNotificationConverter;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final static int DELAY = 5000;
    private final static String DEFAULT_BOARD = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    @SneakyThrows
    @Override
    public GameInitResponse initGame(GameInitRequest initReq) {
        String gameCode = GameInitUtil.generateGameCode();
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

        if(isAIFlow) {
            Thread.sleep(DELAY);
            AIMessageEventData eventData =
                    new AIMessageEventData(this, gameDTO.getGameCode(), DEFAULT_BOARD);
            applicationEventPublisher.publishEvent(eventData); //async
        } else {
            GameInitNotification notification = gameInitNotificationConverter.toGameInitNotification(gameDTO);
            ChatServiceEventData eventData =
                    new ChatServiceEventData(this, notification);
            applicationEventPublisher.publishEvent(eventData); //async
        }

        return new GameInitResponse(gameCode);
    }

}
