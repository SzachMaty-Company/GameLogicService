package com.szachmaty.gamelogicservice.config.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
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

}
