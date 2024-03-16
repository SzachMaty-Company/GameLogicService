package com.szachmaty.gamelogicservice.infrastructure.controller.validations;

import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameInitReq;
import com.szachmaty.gamelogicservice.infrastructure.controller.ws.GameMessage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class RequestValidatorImpl implements ConstraintValidator<RequestValidator, Object> {

    /**
     * Checks if format is valid (h:min:sec)
     */
//    private static final String GAME_TIME_REGEX = "^([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
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
        if(input instanceof GameInitReq) {
            return checkGameCreateRequest((GameInitReq) input);
        } else if(input instanceof GameMessage) {
          return checkGameMessage((GameMessage) input);
        } else {
            throw new RuntimeException("Cannot find input type!");
        }
    }

    private boolean checkGameCreateRequest(GameInitReq input) {
        if(input == null) {
            return false;
        }
        boolean isModeValid = StringUtils.containsAny(input.gameMode(), "FRIEND", "AI");
        boolean isGameTimeValid = input.gameTime() != null && input.gameTime() > 0;
        boolean isPlayersValid =  !input.player1().isBlank() && !input.player2().isBlank();
        boolean isColorValid = StringUtils.containsAny(input.player1PieceColor(), "WHITE","BLACK");
        return isModeValid && isPlayersValid && isGameTimeValid && isColorValid;
    }

    private boolean checkGameMessage(GameMessage input) {
        return !EMPTY_STRING.equals(input.getUserId()) && !EMPTY_STRING.equals(input.getMove())
                && !EMPTY_STRING.equals(input.getGameCode());
    }
}
