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
        GameProcessDTO  gameProcessDTO = new GameProcessDTO();
        gameProcessDTO.setGameCode(message.getGameCode());
        gameProcessDTO.setMove(message.getMove());

        GameDTO gameDTO = gameDTOManager.getBoards(gameProcessDTO.getGameCode());
        List<String> boards = gameDTO.getBoardStateList();

        if(boards != null) {
            Side side = boards.size() % 2 == 0 ? Side.WHITE : Side.BLACK;
            String currBoardState = boards.size() == 0 ? INIT_CHESS_BOARD : boards.get(boards.size() - 1);
            gameProcessDTO.setSide(side);
            gameProcessDTO.setCurrBoardState(currBoardState);
        } else { //first move
            gameProcessDTO.setFirstMove(true);
            gameProcessDTO.setSide(Side.WHITE);
            gameProcessDTO.setCurrBoardState(INIT_CHESS_BOARD);
        }
        
        boolean isMoveValid = moveValidator.doMove(gameProcessDTO);

        if(isMoveValid) {
            String afterMoveBoardState = moveValidator.getBoardState();
            LinkedList<Long> gameHistory = moveValidator.getHistory();
            gameProcessDTO.setAfterMoveBoardState(afterMoveBoardState);
            gameProcessDTO.setGameHistory(gameHistory);
        } else {
            throw new InvalidMoveException("Move: " + gameProcessDTO.getMove() + " is invalid!");
        }

        if(gameProcessDTO.getAfterMoveBoardState() != null) {
            updateTime(gameDTO, gameProcessDTO);
            GameFinishDTO boardStateFinish = gameFinishDetector
                    .checkResultBasedOnBoard(gameProcessDTO);
            GameFinishDTO timeFinish = gameFinishDetector
                    .checkResultBasedOnTime(gameProcessDTO);

            if(boardStateFinish != null && boardStateFinish.isFinish()) {
                GameDTO game = gameDTOManager.updateBoard(gameProcessDTO);
                notifyUserDataService(game);
                return gameProcessDTO.getAfterMoveBoardState();
            } else if(timeFinish != null && timeFinish.isFinish()) {
                GameDTO game = gameDTOManager.updateBoard(gameProcessDTO);
                notifyUserDataService(game);
                return gameProcessDTO.getAfterMoveBoardState();
            }

            gameDTOManager.updateBoard(gameProcessDTO);
            return gameProcessDTO.getAfterMoveBoardState();
        } else {
            throw new InvalidMoveException("Move: " + gameProcessDTO.getMove() + " is invalid!");
        }
    }

    private void updateTime(GameDTO gameDTO, GameProcessDTO gameProcessDTO) {
        if(gameProcessDTO.isFirstMove()) {
           gameProcessDTO.setPrevMoveTime(gameDTO.getPrevMoveTime());
           gameProcessDTO.setWhiteTime(gameDTO.getWhiteTime());
           gameProcessDTO.setBlackTime(gameDTO.getBlackTime());
           return;
        }

        Side side = gameProcessDTO.getSide();
        Long prevTime = gameDTO.getPrevMoveTime();
        Long playerGameTime = side == Side.WHITE ? gameDTO.getWhiteTime() : gameDTO.getBlackTime();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Long currTime = timestamp.getTime();
        Long diffBetweenTimeStamps = currTime - prevTime;
        Long updatedGameTime = playerGameTime - diffBetweenTimeStamps;

        if(side == Side.WHITE) {
            gameProcessDTO.setWhiteTime(updatedGameTime);
        } else {
            gameProcessDTO.setBlackTime(updatedGameTime);
        }
        gameProcessDTO.setPrevMoveTime(currTime);
    }


    private void notifyUserDataService(GameDTO game) {
        gameClient.sendGameAfterFinish(game);
        gameDTOManager.deleteGame(game);
    }
}
