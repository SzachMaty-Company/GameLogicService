package com.szachmaty.gamelogicservice.infrastructure.controller.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import com.szachmaty.gamelogicservice.application.manager.GameOperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationProvider implements AuthenticationProvider {

    private final GameOperationService gameOperationService;
    private static final String USER_ID_CLAIM = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier";

    @Value("${jwt.key}")
    private String jwtKey;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AuthenticationToken authenticationToken = (AuthenticationToken) authentication;
        String rawToken = (String) authenticationToken.getPrincipal();

        if(rawToken.startsWith("Bearer ")) {
            rawToken = rawToken.substring(7);
        }

        String userId = null;
        boolean isTokenValid = false;
        try {
            SignedJWT jwt = (SignedJWT) JWTParser.parse(rawToken);
            JWSVerifier verifier = new MACVerifier(jwtKey.getBytes());
            isTokenValid = jwt.verify(verifier);

            userId = jwt.getJWTClaimsSet().getStringClaim(USER_ID_CLAIM);
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }

        boolean isUserValid = gameOperationService
                .isPlayerGameParticipant((String) authenticationToken.getCredentials(), userId);
        if(isUserValid && isTokenValid) {
            authenticationToken.setAuthenticated(true);
        }
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AuthenticationToken.class.equals(authentication);
    }
}
