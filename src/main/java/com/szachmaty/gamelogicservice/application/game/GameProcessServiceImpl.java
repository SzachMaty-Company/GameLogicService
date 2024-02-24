package com.szachmaty.gamelogicservice.application.game;

import com.github.bhlangonijr.chesslib.Board;
import com.szachmaty.gamelogicservice.application.manager.GameDTOManager;
import com.szachmaty.gamelogicservice.domain.dto.GameDTO;
import com.szachmaty.gamelogicservice.infrastructure.controller.apiclient.GameClient;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameMoveInfoMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameProcessServiceImpl implements GameProcessService {

    private final MoveProcessor moveValidator;
    private final GameDTOManager gameDTOManager;
    private final GameClient gameClient;

    @Override
    public String process(String move, String gameCode) {
        String currBoardState = moveValidator.doMove(move, gameCode);
        if(currBoardState != null) {
            Board board = new Board();
            board.loadFromFen(currBoardState);
            boolean isDraw = board.isDraw();
            boolean isMated = board.isMated();
            boolean isFinished = isDraw || isMated;
            if(isDraw) {
                GameDTO game = gameDTOManager.updateBoard(move, currBoardState, gameCode, isFinished);
                notifyUserDataService(game);
                return currBoardState;
            }
            if(isMated) {
                GameDTO game = gameDTOManager.updateBoard(move, currBoardState, gameCode, isFinished);
                notifyUserDataService(game);
                return currBoardState;
            }
            gameDTOManager.updateBoard(move, currBoardState, gameCode, isFinished);
            return currBoardState;
        } else {
            throw new InvalidMoveException("Move: " + move + " is invalid!");
        }
    }

    private void notifyUserDataService(GameDTO game) {
        gameClient.sendGameAfterFinish(game);
        gameDTOManager.deleteGame(game);
    }
}
