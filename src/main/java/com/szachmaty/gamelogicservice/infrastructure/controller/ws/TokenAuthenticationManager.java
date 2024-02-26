package com.szachmaty.gamelogicservice.infrastructure.controller.ws;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.szachmaty.gamelogicservice.application.manager.GameDTOManager;
import com.szachmaty.gamelogicservice.domain.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationManager {

    private final GameDTOManager gameDTOManager;
    private static final String USER_ID_CLAIM = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier";
    private static final String USERNAME_CLAIM = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name";

    public boolean authenticate(String token) {
        String userID;
        try {
            JWT jwt = JWTParser.parse(token);
            userID = jwt.getJWTClaimsSet().getStringClaim(USER_ID_CLAIM);
        } catch (ParseException e) {
            throw new RuntimeException("Cannot parse JWT token!");
        }
        UserDTO userDTO = gameDTOManager.getUserById(userID);
        return true;
    }
}
