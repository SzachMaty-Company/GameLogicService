package com.szachmaty.gamelogicservice.service.gameinit;

import com.szachmaty.gamelogicservice.controller.apiclient.ChatServiceClient;
import com.szachmaty.gamelogicservice.data.dto.GameInitNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceCallPreparatorImpl implements ChatServiceCallPreparator {

    private final static String GAME_EVENT_ERROR = "GameInitNotification is null!";
    private final static String CHAT_SERVICE_ERROR = "Player doesn't exists!";

    private final ChatServiceClient chatServiceClient;

    public void notifyChatService(GameInitNotification notification) {
        if(notification == null) {
            throw new GameInitException(GAME_EVENT_ERROR);
        }

        try {
            chatServiceClient.iniviteToGame(notification);
        } catch(Exception e) {
            throw new GameInitException(CHAT_SERVICE_ERROR);
        }

    }

}
