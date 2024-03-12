package com.szachmaty.gamelogicservice.infrastructure.controller.ws;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.function.Supplier;

public class WebSocketAuthenticatedAuthorizationManager<T> implements AuthorizationManager<T> {

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, T object) {
        Authentication auth = authentication.get();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        if(auth.isAuthenticated() && authorities != null) {
            boolean isGrantedAuthority = authorities.stream()
                                .anyMatch(grantedAuthority  ->
                                        "ROLE_USER".equals(grantedAuthority != null ? grantedAuthority.getAuthority() : null));
            if(isGrantedAuthority) {
                return new AuthorizationDecision(true);
            }
        }
        return new AuthorizationDecision(false);
    }
}
