package com.szachmaty.gamelogicservice.service.game.chain;

import com.szachmaty.gamelogicservice.data.dto.GameProcessContext;
import com.szachmaty.gamelogicservice.service.game.chain.X.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameChainFactoryImpl implements GameChainFactory {

    private final MovePreparer movePreparer;
    private final MoveValidator moveValidator;
    private final TimeUpdater timeUpdater;
    private final FinishDetector finishDetector;
    private final FinishNotificator finishNotificator;

    @Override
    public GameChainList createChainForGame(GameProcessContext context) {
        GameChainList gameChainList = new GameChainList();
        return gameChainList.addNext(movePreparer)
                .addNext(moveValidator)
                .addNext(timeUpdater)
                .addNext(finishDetector)
                .addNext(finishNotificator);
    }
}
