package com.szachmaty.gamelogicservice.service.gameinfo;

import com.github.bhlangonijr.chesslib.Side;
import com.szachmaty.gamelogicservice.data.dto.GameDTO;
import com.szachmaty.gamelogicservice.data.dto.GameInfoDTO;
import com.szachmaty.gamelogicservice.exception.GameException;
import com.szachmaty.gamelogicservice.repository.GameOperationService;
import com.szachmaty.gamelogicservice.service.game.TimeProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class GameInfoServiceImpl implements GameInfoService {

    private final GameOperationService gameOperationService;
    private final TimeProcessor timeProcessor;

    private final static String DEFAULT_BOARD = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    @Override
    public GameInfoDTO getGameByGameCode(String gameCode) {
        if("".equals(gameCode)) {
            throw new GameException("Game with given gameCode: " + gameCode + " doesn't exists!");
        }

        //check if user belongs to that game - TO DO
        GameDTO gameDTO = gameOperationService.getGameByGameCode(gameCode);
        if(gameDTO == null) {
            throw new GameException("Game with given gameCode: " + gameCode + " doesn't exists!");
        }

        boolean isFirstMove = gameDTO.getFenList() == null || gameDTO.getFenList().isEmpty();
        if(isFirstMove) {
            return processWhenFirstMove(gameDTO);
        }

        Side sideToMove = gameDTO.getSideToMove();
        Long playerTime;
        if(sideToMove == Side.WHITE) {
            playerTime = gameDTO.getWhiteTime();
        } else {
            playerTime = gameDTO.getBlackTime();
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Long currSystemTime = timestamp.getTime();
        Long updatedTime = timeProcessor.countTime(false, gameDTO.getPrevSystemTime(), currSystemTime, playerTime);

        GameInfoDTO gameInfoDTO = getGameInfoDTO(sideToMove, updatedTime, gameDTO);
        return gameInfoDTO;
    }

    private GameInfoDTO getGameInfoDTO(Side sideToMove, Long updatedTime, GameDTO gameDTO) {
        GameInfoDTO gameInfoDTO = new GameInfoDTO();
        if(sideToMove == Side.WHITE) {
            gameInfoDTO.setWhiteTime(updatedTime);
            gameInfoDTO.setBlackTime(gameDTO.getBlackTime());
        } else {
            gameInfoDTO.setWhiteTime(gameDTO.getWhiteTime());
            gameInfoDTO.setBlackTime(updatedTime);
        }
        int lastBoardIndex = gameDTO.getFenList().size();
        gameInfoDTO.setFen(gameDTO.getFenList().get(lastBoardIndex - 1));
        gameInfoDTO.setGameStatus(gameDTO.getGameStatus());
        gameInfoDTO.setSideToMove(sideToMove.name());
        return gameInfoDTO;
    }

    private GameInfoDTO processWhenFirstMove(GameDTO gameDTO) {
        GameInfoDTO gameInfoDTO = new GameInfoDTO();
        gameInfoDTO.setFen(DEFAULT_BOARD);
        gameInfoDTO.setWhiteTime(gameDTO.getWhiteTime());
        gameInfoDTO.setBlackTime(gameDTO.getBlackTime());
        gameInfoDTO.setGameStatus(gameDTO.getGameStatus());
        gameInfoDTO.setSideToMove(gameDTO.getSideToMove().name());
        return gameInfoDTO;
    }

}
