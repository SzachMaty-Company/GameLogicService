package com.szachmaty.gamelogicservice.controller;

import com.szachmaty.gamelogicservice.data.dto.GameDTO;
import com.szachmaty.gamelogicservice.data.dto.MoveResponseDTO;
import com.szachmaty.gamelogicservice.exception.GameClientException;
import com.szachmaty.gamelogicservice.exception.GameException;
import com.szachmaty.gamelogicservice.repository.GameOperationService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.ConnectException;

import static com.szachmaty.gamelogicservice.controller.APIRoutes.QUEUE_URL;

@RestControllerAdvice
@RequiredArgsConstructor
public class GameExceptionHandler {

    private final static String INIT_CHESS_BOARD = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final GameOperationService gameOperationService;

    @ExceptionHandler({ GameClientException.class })
    @SuppressWarnings("rawtypes")
    public ResponseEntity gameClientExceptionHandler(GameClientException e) {
        return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }
    @ExceptionHandler({ FeignException.class })
    @SuppressWarnings("rawtypes")
    public ResponseEntity connectionErrorHandler(ConnectException e) {
        return new ResponseEntity("Network Error!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler({ GameException.class })
    @SuppressWarnings("rawtypes")
    public ResponseEntity gameException(GameException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @MessageExceptionHandler
    public void handeExceptions(Exception e) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication auth = securityContext.getAuthentication();

        String gameCode = (String) auth.getCredentials();
        GameDTO gameDTO = gameOperationService.getGameByGameCode(gameCode);

        String destination = QUEUE_URL + gameCode;
        String lastBoardState = INIT_CHESS_BOARD;
        String lastMove = null;
        String nextPlayerMove = gameDTO.getSideToMove().name();

        if(gameDTO != null) {
            if(gameDTO.getBoardStateList() != null && !gameDTO.getBoardStateList().isEmpty()) {
                lastBoardState = gameDTO.getBoardStateList().get(gameDTO.getBoardStateList().size() - 1);
            }
            if(gameDTO.getMoveList() != null && !gameDTO.getMoveList().isEmpty()) {
                lastMove = gameDTO.getMoveList().get(gameDTO.getMoveList().size() - 1);
            }
        }

        MoveResponseDTO moveResponseDTO = new MoveResponseDTO(
                lastMove,
                lastBoardState,
                null,
                nextPlayerMove,
                e.getMessage()
        );
        simpMessagingTemplate.convertAndSend(destination, moveResponseDTO);
    }

}
