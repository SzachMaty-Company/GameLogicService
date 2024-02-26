package com.szachmaty.gamelogicservice.infrastructure.controller.ws;

import com.szachmaty.gamelogicservice.infrastructure.controller.data.TokenException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.authenticator.BasicAuthenticator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthenticationChannelInterceptor implements ChannelInterceptor {

    private final TokenAuthenticationManager authenticationManager;
    private final static String TOKEN_HEADER = "token";


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor stompHeaderAccessor =  MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if(stompHeaderAccessor != null && stompHeaderAccessor.getCommand() != null
                && !stompHeaderAccessor.getCommand().equals(StompCommand.CONNECT)) {
            return message;
        }
        List<String> token = stompHeaderAccessor.getNativeHeader(TOKEN_HEADER);

        if(token == null) {
            throw new TokenException("Token is null!");
        }
        if(token.size() != 1) {
            throw new TokenException("Should be one token!");
        }

        authenticationManager.authenticate(token.get(0));
        return message;
    }
}
