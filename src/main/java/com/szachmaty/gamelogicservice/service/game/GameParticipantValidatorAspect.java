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

    @Before("@annotation(GameParticipantValidator)")
    public void validate(JoinPoint joinPoint) {
        List<Object> args = Arrays.stream(joinPoint.getArgs()).toList();
        if(args.size() == 1) {
            Object arg1 = args.get(0);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication auth = securityContext.getAuthentication();
            if(auth == null) {
                log.error("Authentication is null!");
                throw new BadCredentialsException("Bad credentials!");
            }
            String principal = (String) auth.getPrincipal();
            if(principal == null) {
                log.error("Principal is null!");
                throw new BadCredentialsException("Bad credentials!");
            }

            if(arg1 instanceof GameMessage gameMessage) {
                boolean isValid = gameOperationService
                        .isPlayerGameParticipant(gameMessage.getGameCode(), principal);
                if(!isValid) {
                    log.error("Player is not this game participant!");
                    throw new BadCredentialsException("Player is not this game participant!");
                }

                boolean isPlayerTurn = gameOperationService
                        .validatePlayerTurn(gameMessage.getGameCode(), principal);
                if(!isPlayerTurn) {
                    log.error("Incorrect turn - Now it's opponent turn");
                    throw new BadCredentialsException("Incorrect turn - Now it's opponent turn!");
                }
            } else {
                throw new BadCredentialsException("Bad credentials!");
            }
        } else {
            throw new BadCredentialsException("Bad credentials");
        }
    }

}
