package com.szachmaty.gamelogicservice.service.gameinit;

import com.szachmaty.gamelogicservice.data.dto.GameDTO;
import com.szachmaty.gamelogicservice.data.dto.GameInitNotification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class GameInitNotificationConverterImpl implements GameInitNotificationConverter {

    public GameInitNotification toGameInitNotification(GameDTO gameDTO) {
        return GameInitNotification.builder()
                .inviteSenderId(resolveSender())
                .inviteReceiverId(resolveReceiver(gameDTO))
                .gameCode(gameDTO.getGameCode())
                .build();
    }

    private String resolveSender() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (String) authentication.getPrincipal();
    }

    private String resolveReceiver(GameDTO gameDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String) authentication.getPrincipal();
        String whiteUserId = gameDTO.getWhiteUserId();
        String blackUserId = gameDTO.getBlackUserId();
        return whiteUserId.equals(userId) ? blackUserId : whiteUserId;
    }
}
