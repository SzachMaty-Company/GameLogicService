package com.szachmaty.gamelogicservice.service.event;

import com.szachmaty.gamelogicservice.controller.apiclient.UserDataServiceClient;
import com.szachmaty.gamelogicservice.data.dto.GameDTO;
import com.szachmaty.gamelogicservice.data.dto.GameFinishDTO;
import com.szachmaty.gamelogicservice.exception.GameClientException;
import com.szachmaty.gamelogicservice.exception.GameException;
import com.szachmaty.gamelogicservice.repository.GameOperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceEventListener implements ApplicationListener<UserServiceEventData> {

    private final GameOperationService gameOperationService;
    private final UserDataServiceClient userServiceClient;
    private final static String GAME_EVENT_ERROR = "GameDTO is null!";
    private final static String USER_SERVICE_CLIENT_ERORR = "UserService client error!";


    @Override
    public void onApplicationEvent(UserServiceEventData event) {
        if(event.getGameDTO() == null) {
            throw new GameException(GAME_EVENT_ERROR);
        }
        GameFinishDTO gameFinishDTO = buildGameFinishDTO(event.getGameDTO());

        try {
            userServiceClient.sendGame(gameFinishDTO);
        } catch(Exception e) {
            log.info(e.getMessage());
            throw new GameClientException(USER_SERVICE_CLIENT_ERORR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        gameOperationService.deleteGameByGameCode(gameFinishDTO.getGameCode());
    }

    private GameFinishDTO buildGameFinishDTO(GameDTO game) {
        return GameFinishDTO.builder()
                .gameCode(game.getGameCode())
                .whiteUserId(game.getWhiteUserId())
                .blackUserId(game.getBlackUserId())
                .gameMode(game.getGameMode())
                .gameTime(game.getGameDuration())
                .gameStartTime(game.getGameStartTime())
                .gameStatus(game.getGameStatus())
                .fenList(game.getFenList())
                .moveList(game.getMoveList())
                .build();
    }
}
