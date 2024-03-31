package com.szachmaty.gamelogicservice.config.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class AuthenticationToken extends AbstractAuthenticationToken {

    private static final Collection<GrantedAuthority> DEFAULT_AUTHORITIES = List.of(
            new SimpleGrantedAuthority("ROLE_USER")
    );
    private Object principal;
    // represents gameCode
    private Object credentials;

    public AuthenticationToken(Object principal) {
        super(DEFAULT_AUTHORITIES);
        this.principal = principal;
    }

    public AuthenticationToken(Object principal, Object credentials) {
        super(DEFAULT_AUTHORITIES);
        this.principal = principal;
        this.credentials = credentials;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    public void setPrincipal(Object principal) {
        this.principal = principal;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }


}
