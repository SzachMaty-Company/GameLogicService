package com.szachmaty.gamelogicservice.service.game.external;

import com.szachmaty.gamelogicservice.controller.apiclient.ChatServiceClient;
import com.szachmaty.gamelogicservice.data.dto.GameDTO;
import com.szachmaty.gamelogicservice.data.dto.GameInitNotification;
import com.szachmaty.gamelogicservice.service.gameinit.GameInitException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceEventListener implements ApplicationListener<ChatServiceEventData> {

    private final static String GAME_EVENT_ERROR = "GameDTO is null!";
    private final static String CHAT_SERVICE_ERROR = "Player doesn't exists!";

    private final ChatServiceClient chatServiceClient;

    @Override
    public void onApplicationEvent(ChatServiceEventData event) {
        if(event.getGameDTO() == null) {
            throw new GameInitException(GAME_EVENT_ERROR);
        }

        GameInitNotification gameInitNotification = buildGameInitNotification(event.getGameDTO());

        try {
            chatServiceClient.iniviteToGame(gameInitNotification);
        } catch(Exception e) {
            throw new GameInitException(CHAT_SERVICE_ERROR);
        }

    }

    private GameInitNotification buildGameInitNotification(GameDTO gameDTO) {
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
        return whiteUserId.equals(userId) ? userId : blackUserId;
    }
}
