package com.szachmaty.gamelogicservice.service.game;

import com.szachmaty.gamelogicservice.controller.apiclient.AIClient;
import com.szachmaty.gamelogicservice.data.dto.*;
import com.szachmaty.gamelogicservice.exception.GameClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import static com.szachmaty.gamelogicservice.controller.APIRoutes.QUEUE_URL;

@Service
@RequiredArgsConstructor
public class GameAIEventListener implements ApplicationListener<GameAIMessageEventData> {

    private final AIClient aiClient;
    private final GameProcessService gameProcessService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void onApplicationEvent(GameAIMessageEventData event) {
        String gameCode = event.getGameCode();
        String board = event.getBoard();
        AIDataResponse dataResponse = null;
        AIDataRequest aiDataRequest = new AIDataRequest(board);
        try {
            dataResponse = aiClient.makeMove(aiDataRequest);
        } catch(Exception e) {
            throw new GameClientException("AI client error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(dataResponse == null) {
            throw new GameClientException("AI client error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        GameMessage gameMessage = GameMessage.builder()
                .userId(GameMode.AI.name())
                .gameCode(event.getGameCode())
                .move(dataResponse.getMove())
                .build();
        MoveResponseDTO moveResponseDTO = gameProcessService.processMove(gameMessage);
        String destination = QUEUE_URL + gameCode;
        simpMessagingTemplate.convertAndSend(destination, moveResponseDTO);
    }
}
