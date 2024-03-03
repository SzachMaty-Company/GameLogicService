package com.szachmaty.gamelogicservice.application.game;

import com.github.bhlangonijr.chesslib.Side;
import com.szachmaty.gamelogicservice.application.manager.GameOperationService;
import com.szachmaty.gamelogicservice.domain.dto.GameDTO;
import com.szachmaty.gamelogicservice.infrastructure.controller.apiclient.GameClient;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameFinishDTO;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.MoveResponseDTO;
import com.szachmaty.gamelogicservice.infrastructure.controller.ws.GameMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameProcessServiceImpl implements GameProcessService {

    private final MoveProcessor moveValidator;
    private final GameOperationService gameOperationService;
    private final GameClient gameClient;
    private final GameFinishDetector gameFinishDetector;
    private final static String INIT_CHESS_BOARD = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    @Override
    @GameParticipantValidator
    public MoveResponseDTO process(GameMessage message) {
        GameProcessDTO  gameProcessDTO = new GameProcessDTO();
        gameProcessDTO.setGameCode(message.getGameCode());
        gameProcessDTO.setMove(message.getMove());

        GameDTO gameDTO = gameOperationService.getBoards(gameProcessDTO.getGameCode());
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

        try {
            boolean isMoveValid = moveValidator.doMove(gameProcessDTO);
            if(isMoveValid) {
                String afterMoveBoardState = moveValidator.getBoardState();
                LinkedList<Long> gameHistory = moveValidator.getHistory();
                gameProcessDTO.setAfterMoveBoardState(afterMoveBoardState);
                gameProcessDTO.setGameHistory(gameHistory);
            } else {
                throw buildInvalidMoveException(gameProcessDTO, gameDTO);
            }
        } catch(Exception e) {
            throw buildInvalidMoveException(gameProcessDTO, gameDTO);
        }

        if(gameProcessDTO.getAfterMoveBoardState() != null) {
            updateTime(gameDTO, gameProcessDTO);
            GameFinishDTO boardStateFinish = gameFinishDetector
                    .checkResultBasedOnBoard(gameProcessDTO);
            GameFinishDTO timeFinish = gameFinishDetector
                    .checkResultBasedOnTime(gameProcessDTO);

            if((boardStateFinish != null && boardStateFinish.isFinish()) ||
                    timeFinish != null && timeFinish.isFinish() ) {
                GameDTO game = gameOperationService.updateBoard(gameProcessDTO);
                notifyUserDataService(game);
                return gameProcessToMoveResponeConverter(gameProcessDTO);
            }

            gameOperationService.updateBoard(gameProcessDTO);
            return gameProcessToMoveResponeConverter(gameProcessDTO);
        } else {
            throw buildInvalidMoveException(gameProcessDTO, gameDTO);
        }
    }

    private MoveResponseDTO gameProcessToMoveResponeConverter(GameProcessDTO gameProcessDTO) {
        Long time;
        if(gameProcessDTO.getSide() == Side.WHITE) {
            time = gameProcessDTO.getWhiteTime();
        } else {
            time = gameProcessDTO.getBlackTime();
        }
        return new MoveResponseDTO(gameProcessDTO.getMove(), gameProcessDTO.getAfterMoveBoardState(), time);
    }

    private void updateTime(GameDTO gameDTO, GameProcessDTO gameProcessDTO) {
        if(gameProcessDTO.isFirstMove()) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            gameProcessDTO.setPrevMoveTime(timestamp.getTime());
            gameProcessDTO.setWhiteTime(gameDTO.getWhiteTime());
            gameProcessDTO.setBlackTime(gameDTO.getBlackTime());
            return;
        }

        Side side = gameProcessDTO.getSide();
        Long prevTime = gameDTO.getPrevMoveTime();
        Long playerGameTime = side == Side.WHITE ? gameDTO.getWhiteTime() : gameDTO.getBlackTime();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Long currTime = timestamp.getTime();
        float diffBetweenTimeStampsInMiliSec = currTime - prevTime;
        long diffBetweenTimeStampsInSec = Math.round(diffBetweenTimeStampsInMiliSec/1000);
        Long updatedGameTime = playerGameTime - diffBetweenTimeStampsInSec;

        if(side == Side.WHITE) {
            gameProcessDTO.setWhiteTime(updatedGameTime);
        } else {
            gameProcessDTO.setBlackTime(updatedGameTime);
        }
        gameProcessDTO.setPrevMoveTime(currTime);
    }

    private InvalidMoveException buildInvalidMoveException(GameProcessDTO gameProcessDTO, GameDTO gameDTO) {
        return new InvalidMoveException("Move: " + gameProcessDTO.getMove() + " is invalid!",
                gameProcessDTO.getMove(),
                gameProcessDTO.getCurrBoardState(),
                gameProcessDTO.getSide() == Side.WHITE ? gameDTO.getWhiteTime() : gameDTO.getBlackTime(),
                gameProcessDTO.getGameCode()
        );
    }

    private void notifyUserDataService(GameDTO game) {
        System.out.println("Wyslaned do klienta " + game.toString());
//        gameClient.sendGameAfterFinish(game);
        gameOperationService.deleteGameByGameCode(game.getGameCode());
    }
}
