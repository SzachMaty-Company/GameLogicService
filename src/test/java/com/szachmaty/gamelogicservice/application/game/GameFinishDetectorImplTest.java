package com.szachmaty.gamelogicservice.application.game;

import com.github.bhlangonijr.chesslib.Side;
import com.szachmaty.gamelogicservice.domain.dto.GameDTO;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameFinishDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class GameFinishDetectorImplTest {

    private GameFinishDetectorImpl gameFinishDetector;


    @BeforeEach
    public void setup() {
        gameFinishDetector = new GameFinishDetectorImpl();
    }

    @Test
    public void checkResultBasedOnBoard_ShouldGameNotBeFinished() {
        String board = "rnbqkbnr/pppp1ppp/8/4p3/3P4/8/PPP1PPPP/RNBQKBNR w KQkq e6 0 2";
        GameFinishDTO expected = new GameFinishDTO(false, false, false, false);

        GameFinishDTO result = gameFinishDetector.checkResultBasedOnBoard(board, Side.BLACK, null);

        assertEquals(expected, result);
    }

    @Test
    public void checkResultBasedOnBoard_ShouldWhiteWin() {
        String board = "rnbqkbnr/2pp1Qpp/pp6/4p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 4";
        GameFinishDTO expected = new GameFinishDTO(true, false, false, true);

        GameFinishDTO result = gameFinishDetector.checkResultBasedOnBoard(board, Side.WHITE, null);

        assertEquals(expected, result);
    }

    @Test
    public void checkResultBasedOnBoard_ShouldBeDraw() {
        String board = "8/8/8/8/6k1/8/4K3/8 w - - 0 58";
        GameFinishDTO expected = new GameFinishDTO(false, false, true, true);

        GameFinishDTO result = gameFinishDetector.checkResultBasedOnBoard(board, Side.WHITE, null);

        assertEquals(expected, result);
    }

    @Test
    public void checkResultBasedOnTime_WhenPlayersHaveGameTime_ShouldGameNotBeFinished() {
        String board = "rnbqkbnr/pppp1ppp/8/4p3/3P4/8/PPP1PPPP/RNBQKBNR w KQkq e6 0 2";
        GameFinishDTO expected = new GameFinishDTO(false, false, false, false);

        GameDTO gameDTO = GameDTO.builder()
                .whiteTime(100L)
                .blackTime(100L)
                .build();

        GameFinishDTO result = gameFinishDetector.checkResultBasedOnTime(gameDTO, board);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"8/8/8/8/8/k7/5K2/8 b - - 0 29", "1k1b4/8/8/8/8/8/8/4K3 w - - 0 1",
            "1k6/2n5/8/8/8/8/8/4K3 w HAha - 0 1", "1k6/1nn5/8/8/8/8/8/4K3 w - - 0 1"})
    public void checkResultBasedOnTime_WhenWhitePlayerRunOutOfTime_ShouldBeDraw(String board) {
        GameFinishDTO expected = new GameFinishDTO(false, false, true, true);

        GameDTO gameDTO = GameDTO.builder()
                .whiteTime(-2L)
                .blackTime(100L)
                .build();

        GameFinishDTO result = gameFinishDetector.checkResultBasedOnTime(gameDTO, board);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"8/8/8/8/8/k7/3K2Pr/8 w - - 0 27", "8/8/8/8/8/k3K3/5r2/8 b - - 3 29",
            "1k6/1nn5/8/8/8/8/4P3/4K3 w - - 0 1"})
    public void checkResultBasedOnTime_WhenWhitePlayerRunOutOfTime_ShouldBlackWin(String board) {
        GameFinishDTO expected = new GameFinishDTO(false, true, false, true);

        GameDTO gameDTO = GameDTO.builder()
                .whiteTime(-2L)
                .blackTime(100L)
                .build();

        GameFinishDTO result = gameFinishDetector.checkResultBasedOnTime(gameDTO, board);

        assertEquals(expected, result);
    }

    @Test
    public void checkResultBasedOnTime_WhenWhitePlayerRunOutOfTime_ShouldWhiteWin() {
        String board = "8/8/8/8/8/k7/3K2Pr/8 w - - 0 27";
        GameFinishDTO expected = new GameFinishDTO(false, true, false, true);

        GameDTO gameDTO = GameDTO.builder()
                .whiteTime(-2L)
                .blackTime(100L)
                .build();

        GameFinishDTO result = gameFinishDetector.checkResultBasedOnTime(gameDTO, board);

        assertEquals(expected, result);
    }


}