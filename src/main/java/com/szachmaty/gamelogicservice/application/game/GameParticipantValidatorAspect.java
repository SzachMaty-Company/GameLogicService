package com.szachmaty.gamelogicservice.application.game;

import com.szachmaty.gamelogicservice.application.manager.GameDTOManager;
import com.szachmaty.gamelogicservice.infrastructure.controller.ws.GameMessage;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Aspect
public class GameParticipantValidatorAspect {

    private final GameDTOManager gameDTOManager;

    @Before("@annotation(GameParticipantValidator)")
    public void validate(JoinPoint joinPoint) {
        List<Object> args = Arrays.stream(joinPoint.getArgs()).toList();
        System.out.println(args);
        System.out.println(args.get(0));
        if(args.size() == 1) {
            Object arg = args.get(0);
            if(arg instanceof GameMessage gameMessage) {
                boolean isValid = gameDTOManager
                        .isPlayerGameParticipant(gameMessage.getGameCode(), gameMessage.getUserId());
                if(!isValid) {
                    throw new BadCredentialsException("bad credentials!"); //TO RETHINK
                }
            }
        }
    }
}
