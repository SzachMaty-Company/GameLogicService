package com.szachmaty.gamelogicservice.infrastructure.controller;

import com.szachmaty.gamelogicservice.application.service.GameService;
import com.szachmaty.gamelogicservice.domain.dto.GameDTO;
import com.szachmaty.gamelogicservice.domain.dto.GameWPlDTO;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameCreateReq;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameInitResp;
import com.szachmaty.gamelogicservice.infrastructure.controller.validations.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<GameInitResp> createGame(
            @RequestBody
            @RequestValidator
            GameCreateReq gCR
    ) {
        return new ResponseEntity<>(gameService.createGame(gCR), HttpStatus.OK);
    }
    @GetMapping(path = "/games")
    public ResponseEntity<GameWPlDTO> getAllTest() {
        return new ResponseEntity<>(gameService.getGame(), HttpStatus.OK);
    }
}
