package com.szachmaty.gamelogicservice.service.game.chain.X;

import com.github.bhlangonijr.chesslib.Side;
import com.szachmaty.gamelogicservice.data.dto.GameDTO;
import com.szachmaty.gamelogicservice.data.dto.GameProcessContext;
import com.szachmaty.gamelogicservice.exception.GameException;
import com.szachmaty.gamelogicservice.repository.GameOperationService;
import com.szachmaty.gamelogicservice.service.game.chain.GameProcessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovePreparer implements GameProcessHandler {

    private final GameOperationService gameOperationService;
    private final static String INIT_CHESS_BOARD = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    @Override
    public boolean process(GameProcessContext context) {
        GameDTO gameDTO = gameOperationService.getGameByGameCode(context.getGameCode());
        if(gameDTO == null) {
            throw new GameException(String.format("Game with provided code: %s doesnt exists!", context.getGameCode()));
        }

        List<String> boards = gameDTO.getFenList();

        if(boards != null) {
            Side side = boards.size() % 2 == 0 ? Side.WHITE : Side.BLACK;
            String currBoardState = boards.isEmpty() ? INIT_CHESS_BOARD : boards.get(boards.size() - 1);
            context.setSide(side);
            context.setCurrBoardState(currBoardState);
        } else { //first move
            context.setFirstMove(true);
            context.setSide(Side.WHITE);
            context.setCurrBoardState(INIT_CHESS_BOARD);
        }

        context.setPrevSystemTime(gameDTO.getPrevSystemTime());
        context.setWhiteTime(gameDTO.getWhiteTime());
        context.setBlackTime(gameDTO.getBlackTime());

        return true;
    }
}
