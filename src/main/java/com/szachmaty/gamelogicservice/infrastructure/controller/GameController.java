package com.szachmaty.gamelogicservice.infrastructure.controller;

import com.szachmaty.gamelogicservice.application.service.GameService;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameCreateRequest;
import com.szachmaty.gamelogicservice.infrastructure.controller.validations.CustomValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.szachmaty.gamelogicservice.infrastructure.controller.constant.APIRoutes.GAME_INIT;

@RestController
@RequiredArgsConstructor
@Validated
public class GameController {

    private final GameService gameService;

    @PostMapping(path = GAME_INIT)
    public ResponseEntity<String> createGame(
            @RequestBody
            @CustomValidator
            GameCreateRequest gCR
    ) {
        return new ResponseEntity<>(gameService.createGame(gCR), HttpStatus.OK);
    }
}
