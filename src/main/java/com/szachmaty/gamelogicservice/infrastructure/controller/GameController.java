package com.szachmaty.gamelogicservice.infrastructure.controller;

import com.szachmaty.gamelogicservice.application.game.GameProcessService;
import com.szachmaty.gamelogicservice.application.gameinit.GameInitService;
import com.szachmaty.gamelogicservice.domain.dto.GameDTO;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameInitReq;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameInitRes;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.MoveResponseDTO;
import com.szachmaty.gamelogicservice.infrastructure.controller.validations.RequestValidator;
import com.szachmaty.gamelogicservice.infrastructure.controller.ws.GameMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.szachmaty.gamelogicservice.infrastructure.controller.constant.APIRoutes.GAME_INIT;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Validated
public class GameController {

    private final GameInitService gameService;
    private final GameProcessService gameProcessService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    @GetMapping(path = "/game")
    public List<GameDTO> getAllGames() {
        return gameService.getAllGames();
    }

    @PostMapping(path = GAME_INIT)
    public ResponseEntity<GameInitRes> createGame(
            @RequestBody
            @RequestValidator
            GameInitReq gCR
    ) {
        return new ResponseEntity<>(gameService.initGame(gCR), HttpStatus.OK);
    }

    @MessageMapping("/move")
    public void processChessMove(@RequestValidator GameMessage message) {
        MoveResponseDTO moveResponseDTO = gameProcessService.process(message);
        String destination = "/queue/move/" + message.getGameCode();
        simpMessagingTemplate.convertAndSend(destination, moveResponseDTO);
    }


}
