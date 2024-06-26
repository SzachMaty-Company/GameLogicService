package com.szachmaty.gamelogicservice.controller.validations;

import com.szachmaty.gamelogicservice.data.dto.GameInitRequest;
import com.szachmaty.gamelogicservice.data.dto.GameMessage;
import com.szachmaty.gamelogicservice.data.dto.GameMode;
import com.szachmaty.gamelogicservice.service.gameinit.GameInitException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class RequestValidatorImpl implements ConstraintValidator<RequestValidator, Object> {

    private static final String EMPTY_STRING = "";
    private static final String INVALID_TYPE = "Invalid input type!";

    @Override
    public void initialize(RequestValidator validator) {
        ConstraintValidator.super.initialize(validator);
    }

    @Override
    public boolean isValid(Object input, ConstraintValidatorContext context) {
        if(input == null) {
            return false;
        }
        if(input instanceof GameInitRequest) {
            return checkGameCreateRequest((GameInitRequest) input);
        } else if(input instanceof GameMessage) {
          return checkGameMessage((GameMessage) input);
        } else {
            throw new GameInitException(INVALID_TYPE);
        }
    }

    private boolean checkGameCreateRequest(GameInitRequest input) {
        if(input == null) {
            return false;
        }
        boolean isGameTimeValid = input.gameTime() != null && input.gameTime() > 0;
        boolean isPlayersValid =  !input.player1().isBlank() && !input.player2().isBlank();
        boolean isGameModeValid = validateGameMode(input);
        return isGameModeValid && isPlayersValid && isGameTimeValid;
    }

    private boolean validateGameMode(GameInitRequest input) {
        GameMode gameMode = input.gameMode();
        if(GameMode.isAIMode(gameMode)) {
            return input.player1().contains(GameMode.AI.toString()) || input.player2().contains(GameMode.AI.toString());
        }
        return true;
    }

    private boolean checkGameMessage(GameMessage input) {
        return !EMPTY_STRING.equals(input.getUserId()) && !EMPTY_STRING.equals(input.getMove())
                && !EMPTY_STRING.equals(input.getGameCode());
    }
}
