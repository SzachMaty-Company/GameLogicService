package com.szachmaty.gamelogicservice.application.game;

import com.szachmaty.gamelogicservice.application.manager.GameDTOManager;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameMoveInfoMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameProcessServiceImpl implements GameProcessService {

    private final MoveValidator moveValidator;
    private final GameDTOManager gameDTOManager;

    @Override
    public void doMove(GameMoveInfoMessage gameMove, String gameCode) {
        String currBoardState = moveValidator.validateMove(gameMove.move(), gameCode);
        if(currBoardState != null) {
            gameDTOManager.updateBoard(gameMove.move(), currBoardState, gameCode);
//            return currBoardState;
        } else {
//            InValid Move;
        }
    }
}
