package com.szachmaty.gamelogicservice.service.external;

import com.szachmaty.gamelogicservice.controller.apiclient.ChatServiceClient;
import com.szachmaty.gamelogicservice.data.dto.GameInitNotification;
import com.szachmaty.gamelogicservice.service.gameinit.GameInitException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceEventListener implements ApplicationListener<ChatServiceEventData> {

    private final static String GAME_EVENT_ERROR = "GameInitNotification is null!";
    private final static String CHAT_SERVICE_ERROR = "Player doesn't exists!";

    private final ChatServiceClient chatServiceClient;

    @Override
    public void onApplicationEvent(ChatServiceEventData event) {
        GameInitNotification gameInitNotification = event.getGameInitNotification();
        if(gameInitNotification == null) {
            throw new GameInitException(GAME_EVENT_ERROR);
        }

        try {
            chatServiceClient.iniviteToGame(gameInitNotification);
        } catch(Exception e) {
            throw new GameInitException(CHAT_SERVICE_ERROR);
        }

    }

}
