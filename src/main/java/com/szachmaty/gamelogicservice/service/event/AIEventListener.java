package com.szachmaty.gamelogicservice.service.event;

import com.szachmaty.gamelogicservice.controller.apiclient.AIClient;
import com.szachmaty.gamelogicservice.data.dto.AIDataRequest;
import com.szachmaty.gamelogicservice.data.dto.AIDataResponse;
import com.szachmaty.gamelogicservice.data.dto.GameProcessContext;
import com.szachmaty.gamelogicservice.data.dto.MoveResponseDTO;
import com.szachmaty.gamelogicservice.exception.GameClientException;
import com.szachmaty.gamelogicservice.service.game.chain.GameChainFactory;
import com.szachmaty.gamelogicservice.service.game.chain.GameChainList;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import static com.szachmaty.gamelogicservice.controller.APIRoutes.QUEUE_URL;
import static com.szachmaty.gamelogicservice.service.game.GameUtil.contextToMoveRespone;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIEventListener implements ApplicationListener<AIMessageEventData> {

    private final AIClient aiClient;
    private final GameChainFactory gameChainFactory;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final static String AI_CLIENT_ERROR = "AI client error!";
    private final static int DELAY = 10000;

    @Override
    @SneakyThrows
    public void onApplicationEvent(AIMessageEventData event) {
        if(event.isWhiteAndFirstCall()) {
            Thread.sleep(DELAY);
        }
        String gameCode = event.getGameCode();
        String board = event.getBoard();
        AIDataResponse dataResponse = null;
        AIDataRequest aiDataRequest = new AIDataRequest(board);
        try {
            dataResponse = aiClient.makeMove(aiDataRequest);
        } catch(Exception e) {
            log.info(AI_CLIENT_ERROR + " {}", e.getMessage());
            throw new GameClientException(AI_CLIENT_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(dataResponse == null) {
            log.info(AI_CLIENT_ERROR);
            throw new GameClientException(AI_CLIENT_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String move = dataResponse.getMove();

        GameProcessContext context = new GameProcessContext();
        context.setGameCode(gameCode);
        context.setMove(move);
        GameChainList chainList = gameChainFactory.createChainForGame(context);
        chainList.processChain(context);

        MoveResponseDTO moveResponseDTO = contextToMoveRespone(context);

        String destination = QUEUE_URL + gameCode;
        simpMessagingTemplate.convertAndSend(destination, moveResponseDTO);
    }
}
