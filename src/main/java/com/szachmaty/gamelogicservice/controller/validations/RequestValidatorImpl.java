package com.szachmaty.gamelogicservice.controller.validations;

import com.szachmaty.gamelogicservice.data.dto.GameInitRequest;
import com.szachmaty.gamelogicservice.data.dto.GameMessage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class RequestValidatorImpl implements ConstraintValidator<RequestValidator, Object> {

    private static final String EMPTY_STRING = "";

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
            throw new RuntimeException("Cannot find input type!");
        }
    }

    private boolean checkGameCreateRequest(GameInitRequest input) {
        if(input == null) {
            return false;
        }
        boolean isGameTimeValid = input.gameTime() != null && input.gameTime() > 0;
        boolean isPlayersValid =  !input.player1().isBlank() && !input.player2().isBlank();
        return isPlayersValid && isGameTimeValid;
    }

    private boolean checkGameMessage(GameMessage input) {
        return !EMPTY_STRING.equals(input.getUserId()) && !EMPTY_STRING.equals(input.getMove())
                && !EMPTY_STRING.equals(input.getGameCode());
    }
}
