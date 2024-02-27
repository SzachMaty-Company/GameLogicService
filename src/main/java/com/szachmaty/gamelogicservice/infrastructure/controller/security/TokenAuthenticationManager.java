package com.szachmaty.gamelogicservice.infrastructure.controller.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TokenAuthenticationManager implements AuthenticationManager {

    private final TokenAuthenticationProvider authenticationProvider;

    @Override
    public Authentication authenticate(Authentication authentication) {
        if(authenticationProvider.supports(authentication.getClass())) {
            return authenticationProvider.authenticate(authentication);
        } else {
            throw new BadCredentialsException("Cannot authenticate user!");
        }
    }
}
