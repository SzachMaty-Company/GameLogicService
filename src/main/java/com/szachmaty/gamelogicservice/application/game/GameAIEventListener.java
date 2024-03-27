package com.szachmaty.gamelogicservice.application.game;

import com.szachmaty.gamelogicservice.infrastructure.controller.apiclient.AIClient;
import com.szachmaty.gamelogicservice.infrastructure.controller.apiclient.GameClientException;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.MoveResponseDTO;
import com.szachmaty.gamelogicservice.infrastructure.controller.ws.GameMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

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
        String move = null;
        try {
            move = aiClient.makeMove(board);
        } catch(Exception e) {
            throw new GameClientException("AI client error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        GameMessage gameMessage = GameMessage.builder()
                .userId("AI")
                .gameCode(event.getGameCode())
                .move(move)
                .build();
        MoveResponseDTO moveResponseDTO = gameProcessService.process(gameMessage);
        String destination = "/queue/move/" + gameCode;
        simpMessagingTemplate.convertAndSend(destination, moveResponseDTO);
    }
}
