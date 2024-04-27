package com.szachmaty.gamelogicservice.config.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import com.szachmaty.gamelogicservice.repository.GameOperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationProvider implements AuthenticationProvider {

    private final GameOperationService gameOperationService;

    private static final String BEARER  = "Bearer ";
    private static final String USER_ID_CLAIM = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier";
    private static final String CANNOT_PARSE_TOKEN = "Cannot parse JWT token!";
    private static final String INVALID_TOKEN = "Token is invalid!";
    private static final String INVALID_USER = "Inalid user credentials and/or invalid gameCode!";

    @Value("${jwt.key}")
    private String jwtKey;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AuthenticationToken authenticationToken = (AuthenticationToken) authentication;
        String rawToken = (String) authenticationToken.getPrincipal();

        if(rawToken.startsWith(BEARER)) {
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
            throw new BadCredentialsException(CANNOT_PARSE_TOKEN);
        }

        if(!isTokenValid) {
            throw new BadCredentialsException(INVALID_TOKEN);
        }

        if(authenticationToken.isCreatedFromWSCall()) {
            wsAuthenticationProcessing(authenticationToken, userId);
        }

        authenticationToken.setPrincipal(userId);
        authenticationToken.setAuthenticated(true);
        return authenticationToken;
    }

    private void wsAuthenticationProcessing(AuthenticationToken authenticationToken, String userId) {
        boolean isUserValid = gameOperationService
                .isPlayerGameParticipant((String) authenticationToken.getCredentials(), userId);
        if(!isUserValid) {
            throw new BadCredentialsException(INVALID_USER);
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AuthenticationToken.class.equals(authentication);
    }
}
