package com.szachmaty.gamelogicservice.infrastructure.controller;

import com.szachmaty.gamelogicservice.application.game.GameProcessService;
import com.szachmaty.gamelogicservice.application.gameinit.GameInitService;
import com.szachmaty.gamelogicservice.domain.dto.GameWPlDTO;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameMoveInfoMessage;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameInitReq;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameInitResp;
import com.szachmaty.gamelogicservice.infrastructure.controller.validations.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.szachmaty.gamelogicservice.infrastructure.controller.constant.APIRoutes.GAME_INIT;

@RestController
@RequiredArgsConstructor
@Validated
public class GameController {

    private final GameInitService gameService;
    private final GameProcessService gameProcessService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping(path = GAME_INIT)
    public ResponseEntity<GameInitResp> createGame(
            @RequestBody
            @RequestValidator
            GameInitReq gCR
    ) {
        return new ResponseEntity<>(gameService.initGame(gCR), HttpStatus.OK);
    }
    @GetMapping(path = "/games")
    public ResponseEntity<GameWPlDTO> getAllTest() {
        return new ResponseEntity<>(gameService.getGame(), HttpStatus.OK);
    }

    @PostMapping(path = "/move")
    @SuppressWarnings("rawtypes")
    public ResponseEntity doMove(@RequestBody GameMoveInfoMessage infoMessage) {
        gameProcessService.process(infoMessage.move(), infoMessage.gameCode());
        return new ResponseEntity(HttpStatus.OK);
    }

    @SubscribeMapping("/subscribe")
    public String sendOneTimeMessage() {
        return "Connected to the server"; //onready game & start timestamp
    }

    @MessageMapping("/game/{gameCode}")
    @SendTo("/game-queue/{gameCode}")
    public String processChessMove(@PathVariable("gameCode") String gameCode, String move) {
        gameProcessService.process(move, gameCode);
        return move;
    }

    @MessageMapping("/game-handshake")
    public void sendSpecific(
            @Payload Message msg,
            Principal user,
            @Header("simpSessionId") String sessionId) throws Exception {
        System.out.println(sessionId);
        String boardState = gameProcessService.process(msg.getPayload().toString(),"08a6917c9864");
//        simpMessagingTemplate.convertAndSendToUser( "/secured/user/queue/specific-user", out);
    }
}
