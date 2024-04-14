package com.szachmaty.gamelogicservice.service.game.chain.handler;


import com.szachmaty.gamelogicservice.data.dto.GameProcessContext;
import com.szachmaty.gamelogicservice.data.entity.GameStatus;
import com.szachmaty.gamelogicservice.repository.GameOperationService;
import com.szachmaty.gamelogicservice.service.game.chain.service.GameFinishDetector;
import com.szachmaty.gamelogicservice.service.game.chain.GameProcessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.szachmaty.gamelogicservice.service.game.GameUtil.finishDeterminator;

@Service
@RequiredArgsConstructor
public class FinishDetector implements GameProcessHandler {

    private final GameOperationService gameOperationService;
    private final GameFinishDetector gameFinishDetector;

    @Override
    public boolean process(GameProcessContext context) {
        GameStatus boardStateFinish = gameFinishDetector
                .checkResultBasedOnBoard(context);
        context.setGameStatus(boardStateFinish);

        if(finishDeterminator(boardStateFinish)) {
            gameOperationService.updateBoard(context);
            return true;
        }

        GameStatus timeFinish = gameFinishDetector
                .checkResultBasedOnTime(context);
        context.setGameStatus(timeFinish);
        gameOperationService.updateBoard(context);

        return true;
    }

}
