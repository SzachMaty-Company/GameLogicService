package com.szachmaty.gamelogicservice.controller;

import com.szachmaty.gamelogicservice.controller.validations.RequestValidator;
import com.szachmaty.gamelogicservice.data.dto.*;
import com.szachmaty.gamelogicservice.service.game.GamePreparation;
import com.szachmaty.gamelogicservice.service.gameinfo.GameInfoService;
import com.szachmaty.gamelogicservice.service.gameinit.GameInitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Validated
public class GameController {

    private final GameInitService gameInitService;
    private final GameInfoService gameInfoService;
    private final GamePreparation gamePreparation;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping(path = "/game-info/{gameCode}")
    public ResponseEntity<GameInfoDTO> getGameByGameCode(@PathVariable String gameCode) {
        return new ResponseEntity<>(gameInfoService.getGameByGameCode(gameCode), HttpStatus.OK);
    }

    @PostMapping(path = "/game-init")
    public ResponseEntity<GameInitResponse> createGame(
            @RequestBody
            @RequestValidator
            GameInitRequest gCR
    ) {
        return new ResponseEntity<>(gameInitService.initGame(gCR), HttpStatus.OK);
    }

    @MessageMapping("/move")
    public void processChessMove(@RequestValidator GameMessage message) {
        MoveResponseDTO moveResponseDTO = gamePreparation.prepare(message);
        String destination = "/queue/move/" + message.getGameCode();
        simpMessagingTemplate.convertAndSend(destination, moveResponseDTO);
    }

}
