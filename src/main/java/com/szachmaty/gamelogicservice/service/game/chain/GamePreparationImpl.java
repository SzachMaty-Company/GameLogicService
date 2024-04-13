package com.szachmaty.gamelogicservice.service.game.chain;

import com.github.bhlangonijr.chesslib.Side;
import com.szachmaty.gamelogicservice.data.dto.*;
import com.szachmaty.gamelogicservice.exception.GameException;
import com.szachmaty.gamelogicservice.exception.InvalidMoveException;
import com.szachmaty.gamelogicservice.repository.GameOperationService;
import com.szachmaty.gamelogicservice.service.external.AIMessageEventData;
import com.szachmaty.gamelogicservice.service.game.GameParticipantValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import static com.szachmaty.gamelogicservice.service.game.chain.GameUtil.finishDeterminator;

@Service
@RequiredArgsConstructor
public class GamePreparationImpl implements GamePreparation {

    private final GameOperationService gameOperationService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final GameChainFactory gameChainFactory;
    private final static String GAME_FINISH = "Cannot perform move, game is finished!";

    @GameParticipantValidator
    public MoveResponseDTO prepare(GameMessage message) {
        GameDTO gameDTO = gameOperationService.getGameByGameCode(message.getGameCode());
        if(gameDTO == null) {
            throw new GameException(String.format("Game with provided code: %s doesnt exists!", message.getGameCode()));
        }
        if(finishDeterminator(gameDTO.getGameStatus())) {
            throw new InvalidMoveException(GAME_FINISH);
        }

        GameProcessContext context = new GameProcessContext();
        context.setGameCode(message.getGameCode());
        context.setMove(message.getMove());

        GameChainList chainList = gameChainFactory.createChainForGame(context);
        chainList.processChain(context);
        MoveResponseDTO responseDTO = contextToMoveRespone(context);

        if(GameMode.isAIMode(gameDTO.getGameMode())) {
            AIMessageEventData eventData =
                    new AIMessageEventData(this, message.getGameCode(), responseDTO.fen());
            applicationEventPublisher.publishEvent(eventData); //async
            return responseDTO;
        }

        return responseDTO;
    }

    private MoveResponseDTO contextToMoveRespone(GameProcessContext context) {
        Long time;
        Side nextMove;
        if(context.getSide() == Side.WHITE) {
            time = context.getWhiteTime();
            nextMove = Side.BLACK;
        } else {
            time = context.getBlackTime();
            nextMove = Side.WHITE;
        }
        return new MoveResponseDTO(context.getMove(),
                context.getAfterMoveBoardState(), time, nextMove.toString(), context.getGameStatus());
    }
}
