package com.szachmaty.gamelogicservice.application.game;

import com.szachmaty.gamelogicservice.infrastructure.controller.data.MoveResponseDTO;
import com.szachmaty.gamelogicservice.infrastructure.controller.ws.GameMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;


@SpringBootTest
@ActiveProfiles("dev")
class GameProcessServiceImplTest {

    @Autowired
    private GameProcessServiceImpl gameProcessService;

    @Test
    public void process() {
        List<String> moves = Arrays.asList("E2E4", "E7E5", "F1C4", "A7A5", "D1F3", "A5A4", "G1E2", "A4A3", "E1G1", "A3B2","D2D4","B2A1Q","F3F7");
        String gameCode = "750d22fca5bd";
        String usr1 = "user1";
        String usr2 = "user2";
        int numOfMoves = 13;

        int j=0;
        for(int i=0; i<=(numOfMoves+1)/2; i++) {
            GameMessage whitePlayerMove = new GameMessage(usr1, gameCode, moves.get(j));
            MoveResponseDTO whitePlayerResp = gameProcessService.process(whitePlayerMove);
            System.out.println(whitePlayerResp.toString());
            j++;
            if(j >= 13) {
                break;
            }
            GameMessage blackPlayerMove = new GameMessage(usr2, gameCode, moves.get(j));
            MoveResponseDTO blackPlayerResp = gameProcessService.process(blackPlayerMove);
            System.out.println(blackPlayerResp.toString());
            j++;
        }

    }

}