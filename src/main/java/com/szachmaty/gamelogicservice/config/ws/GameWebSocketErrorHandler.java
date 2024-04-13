package com.szachmaty.gamelogicservice.config.ws;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Slf4j
public class GameWebSocketErrorHandler extends StompSubProtocolErrorHandler {

    public GameWebSocketErrorHandler() {
        super();
    }

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        log.error(ex.getCause().getMessage());
        if(ex.getCause() instanceof BadCredentialsException) {
            return super.handleClientMessageProcessingError(clientMessage, ex.getCause());
        }
        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

    @Override
    public Message<byte[]> handleErrorMessageToClient(Message<byte[]> errorMessage) {
        return super.handleErrorMessageToClient(errorMessage);
    }

    private Message<byte[]> prepareErrorMessage(Message<byte[]> clientMessage, Throwable ex) {
        String message = new JSONObject(String.valueOf(clientMessage)).toString();
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);

        byte b[] = new byte[1];
        return MessageBuilder.createMessage(message != null ? message.getBytes() : b, accessor.getMessageHeaders());
    }
}
