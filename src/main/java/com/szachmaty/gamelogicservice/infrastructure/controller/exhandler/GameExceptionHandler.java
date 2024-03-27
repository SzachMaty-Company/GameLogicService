package com.szachmaty.gamelogicservice.infrastructure.controller.exhandler;

import com.szachmaty.gamelogicservice.application.manager.GameOperationService;
import com.szachmaty.gamelogicservice.domain.dto.GameDTO;
import com.szachmaty.gamelogicservice.infrastructure.controller.apiclient.GameClientException;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.MoveResponseDTO;
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

@RestControllerAdvice
@RequiredArgsConstructor
public class GameExceptionHandler {

    private final static String INIT_CHESS_BOARD = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final GameOperationService gameOperationService;

    //TO-BE CHANGED - ONLY FOR TESTS
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

    @MessageExceptionHandler
    public void handeExceptions(Exception e) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication auth = securityContext.getAuthentication();

        String gameCode = (String) auth.getCredentials();
        GameDTO gameDTO = gameOperationService.getBoards(gameCode);

        String destination = "/queue/move/" + gameCode;
        String lastBoardState = INIT_CHESS_BOARD;
        String lastMove = null;
        String nextPlayerMove = "WHITE";

        if(gameDTO != null) {
            if(gameDTO.getBoardStateList() != null && !gameDTO.getBoardStateList().isEmpty()) {
                lastBoardState = gameDTO.getBoardStateList().get(gameDTO.getBoardStateList().size() - 1);
            }
            if(gameDTO.getMoveList() != null && !gameDTO.getMoveList().isEmpty()) {
                lastMove = gameDTO.getMoveList().get(gameDTO.getMoveList().size() - 1);
                if(gameDTO.getMoveList().size() % 2 != 0) {
                    nextPlayerMove = "BLACK";
                }
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
