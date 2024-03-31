package com.szachmaty.gamelogicservice.controller;

import com.szachmaty.gamelogicservice.service.game.GameProcessService;
import com.szachmaty.gamelogicservice.service.gameinit.GameInitService;
import com.szachmaty.gamelogicservice.data.dto.GameDTO;
import com.szachmaty.gamelogicservice.data.dto.GameInitRequest;
import com.szachmaty.gamelogicservice.data.dto.GameInitResponse;
import com.szachmaty.gamelogicservice.data.dto.MoveResponseDTO;
import com.szachmaty.gamelogicservice.controller.validations.RequestValidator;
import com.szachmaty.gamelogicservice.data.dto.GameMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.szachmaty.gamelogicservice.controller.APIRoutes.GAME_INIT;
import static com.szachmaty.gamelogicservice.controller.APIRoutes.QUEUE_URL;

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
    public ResponseEntity<GameInitResponse> createGame(
            @RequestBody
            @RequestValidator
            GameInitRequest gCR
    ) {
        return new ResponseEntity<>(gameService.initGame(gCR), HttpStatus.OK);
    }

    @MessageMapping("/move")
    public void processChessMove(@RequestValidator GameMessage message) {
        MoveResponseDTO moveResponseDTO = gameProcessService.processGame(message);
        String destination = QUEUE_URL + message.getGameCode();
        simpMessagingTemplate.convertAndSend(destination, moveResponseDTO);
    }

}
