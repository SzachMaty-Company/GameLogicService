package com.szachmaty.gamelogicservice.exception;

import org.springframework.security.authentication.BadCredentialsException;

public class TokenException extends BadCredentialsException {
    public TokenException(String message) {
        super(message);
    }
}
