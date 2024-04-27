package com.szachmaty.gamelogicservice.service.game;

import com.szachmaty.gamelogicservice.repository.GameOperationService;
import com.szachmaty.gamelogicservice.data.dto.GameMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@Aspect
public class GameParticipantValidatorAspect {

    private final GameOperationService gameOperationService;

    private final static String BAD_CREDENTIALS = "Bad credentials!";
    private final static String NOT_GAME_PARTICIPANT = "Player is not this game participant!";
    private final static String INCORRECT_TURN = "Incorrect turn - Now it's opponent turn";
    private final static String NULL_AUTHENTICATION = "Authentication is null!";
    private final static String NULL_PRINCIPAL = "Principal is null!";

    @Before("@annotation(GameParticipantValidator)")
    public void validate(JoinPoint joinPoint) {
        List<Object> args = Arrays.stream(joinPoint.getArgs()).toList();
        if(args.size() == 1) {
            Object arg1 = args.get(0);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication auth = securityContext.getAuthentication();
            if(auth == null) {
                log.error(NULL_AUTHENTICATION);
                throw new BadCredentialsException(BAD_CREDENTIALS);
            }
            String principal = (String) auth.getPrincipal();
            if(principal == null) {
                log.error(NULL_PRINCIPAL);
                throw new BadCredentialsException(BAD_CREDENTIALS);
            }

            if(arg1 instanceof GameMessage gameMessage) {
                boolean isValid = gameOperationService
                        .isPlayerGameParticipant(gameMessage.getGameCode(), principal);
                if(!isValid) {
                    log.error(NOT_GAME_PARTICIPANT);
                    throw new BadCredentialsException(NOT_GAME_PARTICIPANT);
                }

                boolean isPlayerTurn = gameOperationService
                        .validatePlayerTurn(gameMessage.getGameCode(), principal);
                if(!isPlayerTurn) {
                    log.error(INCORRECT_TURN);
                    throw new BadCredentialsException(INCORRECT_TURN);
                }
            } else {
                throw new BadCredentialsException(BAD_CREDENTIALS);
            }
        } else {
            throw new BadCredentialsException(BAD_CREDENTIALS);
        }
    }

}
