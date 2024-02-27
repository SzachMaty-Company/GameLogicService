package com.szachmaty.gamelogicservice.application.game;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.szachmaty.gamelogicservice.application.manager.GameDTOManager;
import com.szachmaty.gamelogicservice.domain.dto.GameDTO;
import com.szachmaty.gamelogicservice.infrastructure.controller.apiclient.GameClient;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameFinishDTO;
import com.szachmaty.gamelogicservice.infrastructure.controller.ws.GameMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameProcessServiceImpl implements GameProcessService {

    private final MoveProcessor moveValidator;
    private final GameDTOManager gameDTOManager;
    private final GameClient gameClient;
    private final GameFinishDetector gameFinishDetector;
    private final static String INIT_CHESS_BOARD = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    @Override
    @GameParticipantValidator
    public String process(GameMessage message) {
        String gameCode = message.getGameCode();
        String move = message.getMove();

        GameDTO gameDTO = gameDTOManager.getBoards(gameCode);
        List<String> boards = gameDTO.getBoardStateList();
        Side side;
        String currBoardState;

        if(boards != null) {
            side = boards.size() % 2 == 0 ? Side.WHITE : Side.BLACK;
            currBoardState = boards.size() == 0 ? INIT_CHESS_BOARD : boards.get(boards.size() - 1);
        } else { //first move
            side = Side.WHITE;
            currBoardState = INIT_CHESS_BOARD;
        }
        
        boolean isMoveValid = moveValidator.doMove(move, currBoardState, side);
        String afterMoveBoardState;
        LinkedList<Long> gameHistory;
        if(isMoveValid) {
            afterMoveBoardState = moveValidator.getBoardState();
            gameHistory = moveValidator.getHistory();
        } else {
            throw new InvalidMoveException("Move: " + move + " is invalid!");
        }

        if(afterMoveBoardState != null) {
            updateTime(gameDTO, side);
            GameFinishDTO boardStateFinish = gameFinishDetector
                    .checkResultBasedOnBoard(afterMoveBoardState, side, gameHistory);
            GameFinishDTO timeFinish = gameFinishDetector.checkResultBasedOnTime(gameDTO, afterMoveBoardState);

            if(boardStateFinish != null && boardStateFinish.isFinish()) {
                GameDTO game = gameDTOManager.updateBoard(move, afterMoveBoardState, gameCode, gameHistory, true);
                notifyUserDataService(game);
                return afterMoveBoardState;
            } else if(timeFinish != null && timeFinish.isFinish()) {
                GameDTO game = gameDTOManager.updateBoard(move, afterMoveBoardState, gameCode,gameHistory, true);
                notifyUserDataService(game);
                return afterMoveBoardState;
            }

            gameDTOManager.updateBoard(move, afterMoveBoardState, gameCode, gameHistory, false);
            return afterMoveBoardState;
        } else {
            throw new InvalidMoveException("Move: " + move + " is invalid!");
        }
    }

    private GameDTO updateTime(GameDTO gameDTO, Side side) {
        Long prevTime = gameDTO.getPrevMoveTime();
        Long playerGameTime = side == Side.WHITE ? gameDTO.getWhiteTime() : gameDTO.getBlackTime();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Long currTime = timestamp.getTime();
        Long diffBetweenTimeStamps = currTime - prevTime;
        Long updatedGameTime = playerGameTime - diffBetweenTimeStamps;

        if(side == Side.WHITE) {
            gameDTO.setWhiteTime(updatedGameTime);
        } else {
            gameDTO.setBlackTime(updatedGameTime);
        }
        gameDTO.setPrevMoveTime(currTime);

        return gameDTO;
    }


    private void notifyUserDataService(GameDTO game) {
        gameClient.sendGameAfterFinish(game);
        gameDTOManager.deleteGame(game);
    }
}
