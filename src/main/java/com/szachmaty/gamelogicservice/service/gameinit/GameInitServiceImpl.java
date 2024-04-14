package com.szachmaty.gamelogicservice.service.gameinit;

import com.github.bhlangonijr.chesslib.Side;
import com.szachmaty.gamelogicservice.data.dto.*;
import com.szachmaty.gamelogicservice.data.entity.GameStatus;
import com.szachmaty.gamelogicservice.repository.GameOperationService;
import com.szachmaty.gamelogicservice.service.external.ChatServiceEventData;
import lombok.RequiredArgsConstructor;
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

    @Override
    public GameInitResponse initGame(GameInitRequest initReq) {
        String gameCode = GameInitUtil.generateGameCode();
        Long playersTime = GameInitUtil.gameTimeParser(initReq.gameTime());
        String gameStartTime = GameInitUtil.gameStartTimeCreator();
        boolean isAIFlow = GameMode.isAIMode(initReq.gameMode());

        boolean isPlayer1White = initReq.player1PieceColor().equals(PlayerColor.WHITE);
        String player1 = initReq.player1();
        String player2 = initReq.player2();

        //handle when AI plays white color
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
            GameInitNotification notification = gameInitNotificationConverter.toGameInitNotification(gameDTO);
            ChatServiceEventData eventData =
                    new ChatServiceEventData(this, notification);
            applicationEventPublisher.publishEvent(eventData); //async
        }

        return new GameInitResponse(gameCode);
    }

}
