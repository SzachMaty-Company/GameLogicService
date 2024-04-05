package com.szachmaty.gamelogicservice.service.game.external;

import com.szachmaty.gamelogicservice.controller.apiclient.GameClient;
import com.szachmaty.gamelogicservice.data.dto.GameDTO;
import com.szachmaty.gamelogicservice.repository.GameOperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceEventListener implements ApplicationListener<UserServiceEventData> {

    private final GameClient gameClient;
    private final GameOperationService gameOperationService;

    @Override
    public void onApplicationEvent(UserServiceEventData event) {
        GameDTO game = event.getGameDTO();
        // TO DO call userDataService
        gameOperationService.deleteGameByGameCode(game.getGameCode());

    }
}
