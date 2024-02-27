package com.szachmaty.gamelogicservice.infrastructure.controller.ws;

import com.szachmaty.gamelogicservice.application.manager.GameDTOManager;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorizationChannelInterceptor implements ChannelInterceptor {

    private final GameDTOManager gameDTOManager;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if(accessor != null && accessor.getCommand() != null && accessor.getCommand().equals(StompCommand.SEND)) {
            GameMessage gameMessage = (GameMessage) message.getPayload();
            if(isGameMessageValid(gameMessage)) {
                boolean isAuthorized = gameDTOManager
                        .isPlayerGameParticipant(gameMessage.getGameCode(), gameMessage.getUserId());
                if(isAuthorized) {
                    //authorize
                } else {
                    //not authorized exception
                }
            } else {
                throw new RuntimeException(""); //TO DO
            }

        }
        return message;
    }

    private boolean isGameMessageValid(GameMessage gameMessage) {
        return gameMessage != null && gameMessage.getGameCode() != null && gameMessage.getUserId() != null;
    }
}
