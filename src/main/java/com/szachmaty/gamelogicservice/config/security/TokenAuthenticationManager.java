package com.szachmaty.gamelogicservice.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TokenAuthenticationManager implements AuthenticationManager {

    private final TokenAuthenticationProvider authenticationProvider;
    private final static String AUTHENTICATION_ERROR = "Cannot authenticate user!";

    @Override
    public Authentication authenticate(Authentication authentication) {
        if(authenticationProvider.supports(authentication.getClass())) {
            return authenticationProvider.authenticate(authentication);
        } else {
            throw new BadCredentialsException(AUTHENTICATION_ERROR);
        }
    }
}
