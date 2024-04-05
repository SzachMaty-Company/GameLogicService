package com.szachmaty.gamelogicservice.service.game.external;

import com.szachmaty.gamelogicservice.controller.apiclient.AIClient;
import com.szachmaty.gamelogicservice.data.dto.*;
import com.szachmaty.gamelogicservice.exception.GameClientException;
import com.szachmaty.gamelogicservice.service.game.GameProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import static com.szachmaty.gamelogicservice.controller.APIRoutes.QUEUE_URL;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIEventListener implements ApplicationListener<AIMessageEventData> {

    private final AIClient aiClient;
    private final GameProcessService gameProcessService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final static String AI_CLIENT_ERROR = "AI client error!";

    @Override
    public void onApplicationEvent(AIMessageEventData event) {
        String gameCode = event.getGameCode();
        String board = event.getBoard();
        AIDataResponse dataResponse = null;
        AIDataRequest aiDataRequest = new AIDataRequest(board);
        try {
            dataResponse = aiClient.makeMove(aiDataRequest);
        } catch(Exception e) {
            log.info(AI_CLIENT_ERROR + " " + e.getMessage());
            throw new GameClientException(AI_CLIENT_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(dataResponse == null) {
            log.info(AI_CLIENT_ERROR);
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
