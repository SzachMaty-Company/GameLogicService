package com.szachmaty.gamelogicservice.service.gameinfo;

import com.github.bhlangonijr.chesslib.Side;
import com.szachmaty.gamelogicservice.config.security.AuthenticationToken;
import com.szachmaty.gamelogicservice.data.dto.GameDTO;
import com.szachmaty.gamelogicservice.data.dto.GameInfoDTO;
import com.szachmaty.gamelogicservice.data.dto.PlayerColor;
import com.szachmaty.gamelogicservice.exception.GameException;
import com.szachmaty.gamelogicservice.repository.GameOperationService;
import com.szachmaty.gamelogicservice.service.game.chain.service.TimeProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
            throw new GameException(String.format("Game with given gameCode: %s doesn't exists!", gameCode));
        }

        GameDTO gameDTO = gameOperationService.getGameByGameCode(gameCode);
        if(gameDTO == null) {
            throw new GameException(String.format("Game with given gameCode: %s doesn't exists!", gameCode));
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

        PlayerColor playerColor = resolvePlayerColor(gameDTO);

        return getGameInfoDTO(sideToMove, updatedTime, playerColor, gameDTO);
    }

    private PlayerColor resolvePlayerColor(GameDTO gameDTO) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        AuthenticationToken authentication = (AuthenticationToken) securityContext.getAuthentication();

        PlayerColor playerColor = null;
        if(authentication != null) {
            String principal = authentication.getPrincipal().toString();

            if(gameDTO.getWhiteUserId().equals(principal)) {
                playerColor = PlayerColor.WHITE;
            } else if(gameDTO.getBlackUserId().equals(principal)) {
                playerColor = PlayerColor.BLACK;
            } else {
                throw new GameException(String.format("User: %s doesn't belong to game with code: %s", principal, gameDTO.getGameCode()));
            }
        } else {
            throw new GameException(String.format("Authentication error!, gameCode %s", gameDTO.getGameCode()));
        }
        return playerColor;
    }

    private GameInfoDTO getGameInfoDTO(Side sideToMove, Long updatedTime, PlayerColor playerColor, GameDTO gameDTO) {
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
        gameInfoDTO.setPlayerColor(playerColor);
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
