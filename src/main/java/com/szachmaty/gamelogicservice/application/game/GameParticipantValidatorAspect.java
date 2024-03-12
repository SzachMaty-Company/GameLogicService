package com.szachmaty.gamelogicservice.application.game;

import com.github.bhlangonijr.chesslib.Side;
import com.szachmaty.gamelogicservice.application.manager.GameOperationService;
import com.szachmaty.gamelogicservice.infrastructure.controller.ws.GameMessage;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Aspect
public class GameParticipantValidatorAspect {

    private final GameOperationService gameOperationService;

    @Before("@annotation(GameParticipantValidator)")
    public void validate(JoinPoint joinPoint) {
        List<Object> args = Arrays.stream(joinPoint.getArgs()).toList();
        if(args.size() == 2) {
            Object arg1 = args.get(0);
            Object arg2 = args.get(1);
            if(arg1 instanceof GameMessage gameMessage && arg2 instanceof Authentication authentication) {
                String principal = (String) authentication.getPrincipal();

                boolean isValid = gameOperationService
                        .isPlayerGameParticipant(gameMessage.getGameCode(), principal);
                if(!isValid) {
                    throw new BadCredentialsException("Bad credentials!");
                }
                Side currSide = gameOperationService.getSideToMove(gameMessage.getGameCode());
//                if(cur)
            }
        }
    }

}
