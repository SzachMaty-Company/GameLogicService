package com.szachmaty.gamelogicservice.infrastructure.controller.validations;

import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameCreateRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class RequestValidator implements ConstraintValidator<CustomValidator, Object> {

    /**
     * Checks if format is valid (h:min:sec)
     */
    private static final String GAME_TIME_REGEX = "^([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";

    @Override
    public void initialize(CustomValidator validator) {
        ConstraintValidator.super.initialize(validator);
    }

    @Override
    public boolean isValid(Object input, ConstraintValidatorContext context) {
        if(input == null) {
            return false;
        }
        if(input instanceof GameCreateRequest) {
            return checkGameCreateRequest((GameCreateRequest) input);
        } else {
            throw new RuntimeException("Cannot find input type!");
        }
    }

    private boolean checkGameCreateRequest(GameCreateRequest input) {
        boolean isModeValid = StringUtils.containsAny(input.gameMode(), "FRIEND", "AI");
        boolean isGameTimeValid = Pattern.matches(GAME_TIME_REGEX, input.gameTime());
        boolean isPlayersValid =  !input.player1().isBlank() && !input.player2().isBlank();
        boolean isColorValid = StringUtils.containsAny(input.player1PieceColor(), "WHITE","BLACK");
        return isModeValid && isPlayersValid && isGameTimeValid && isColorValid;
    }
}
