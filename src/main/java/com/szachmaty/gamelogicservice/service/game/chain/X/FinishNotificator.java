package com.szachmaty.gamelogicservice.service.game.chain.X;

import com.szachmaty.gamelogicservice.data.dto.GameDTO;
import com.szachmaty.gamelogicservice.data.dto.GameProcessContext;
import com.szachmaty.gamelogicservice.repository.GameOperationService;
import com.szachmaty.gamelogicservice.service.external.UserServiceEventData;
import com.szachmaty.gamelogicservice.service.game.chain.GameProcessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import static com.szachmaty.gamelogicservice.service.game.chain.GameUtil.finishDeterminator;

@Service
@RequiredArgsConstructor
public class FinishNotificator implements GameProcessHandler {

    private final GameOperationService gameOperationService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public boolean process(GameProcessContext context) {
        if(finishDeterminator(context.getGameStatus())) {
            GameDTO game = gameOperationService.getGameByGameCode(context.getGameCode());
            UserServiceEventData eventData = new UserServiceEventData(this, game);
            applicationEventPublisher.publishEvent(eventData);
        }
        return false;
    }
}
