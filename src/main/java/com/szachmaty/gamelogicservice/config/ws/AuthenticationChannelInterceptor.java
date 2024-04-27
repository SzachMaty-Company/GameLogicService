package com.szachmaty.gamelogicservice.config.ws;

import com.szachmaty.gamelogicservice.config.security.AuthenticationToken;
import com.szachmaty.gamelogicservice.config.security.TokenAuthenticationManager;
import com.szachmaty.gamelogicservice.exception.TokenException;
import lombok.RequiredArgsConstructor;
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
    private final static String GAME_CODE_HEADER = "gameCode";
    private final static String MISSING_HEADERS = "Missing headers!";
    private final static String MISSING_AUTH_TOKEN = "Missing headers!";
    private final static String MISSING_GAME_CODE = "Missing game code!";
    private final static String INVALID_TOKEN_SIZE = "Only one token is required!";
    private final static String INVALID_GAME_CODE_SIZE = "Only one gameCode is required!";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor stompHeaderAccessor =  MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if(stompHeaderAccessor != null && stompHeaderAccessor.getCommand() != null
                && !stompHeaderAccessor.getCommand().equals(StompCommand.CONNECT)) {
            return message;
        }
        if(stompHeaderAccessor == null) {
            throw new TokenException(MISSING_HEADERS);
        }

        List<String> token = stompHeaderAccessor.getNativeHeader(TOKEN_HEADER);
        List<String> gameCode = stompHeaderAccessor.getNativeHeader(GAME_CODE_HEADER);

        if(token == null) {
            throw new TokenException(MISSING_AUTH_TOKEN);
        }
        if(gameCode == null) {
            throw new TokenException(MISSING_GAME_CODE);
        }
        if(token.size() != 1) {
            throw new TokenException(INVALID_TOKEN_SIZE);
        }
        if(gameCode.size() != 1) {
            throw new TokenException(INVALID_GAME_CODE_SIZE);
        }

        AuthenticationToken authentication = new AuthenticationToken(token.get(0), gameCode.get(0));
        authentication.setCreatedFromWSCall(true);
        authenticationManager.authenticate(authentication);
        stompHeaderAccessor.setUser(authentication);

        return message;
    }
}
