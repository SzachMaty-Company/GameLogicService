package com.szachmaty.gamelogicservice.service.game;

import com.github.bhlangonijr.chesslib.Side;
import com.szachmaty.gamelogicservice.data.dto.GameProcessContext;
import com.szachmaty.gamelogicservice.data.entity.GameStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
class GameFinishDetectorImplTest {

    private GameFinishDetectorImpl gameFinishDetector;


    @BeforeEach
    public void setup() {
        gameFinishDetector = new GameFinishDetectorImpl();
    }

    @Test
    public void checkResultBasedOnBoard_ShouldGameNotBeFinished() {
        String board = "rnbqkbnr/3ppQpp/8/ppp5/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 4";
//        String board = "rnbqkbnr/pppp1ppp/8/4p3/3P4/8/PPP1PPPP/RNBQKBNR w KQkq e6 0 2";
        GameStatus expected = GameStatus.WHITE_WINNER;
        GameProcessContext gameProcessContext = new GameProcessContext();
        gameProcessContext.setAfterMoveBoardState(board);
        gameProcessContext.setSide(Side.WHITE);

        GameStatus result = gameFinishDetector.checkResultBasedOnBoard(gameProcessContext);

        assertEquals(expected, result);
    }

//    @Test
//    public void checkResultBasedOnBoard_ShouldWhiteWin() {
//        String board = "rnbqkbnr/2pp1Qpp/pp6/4p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 4";
//        GameFinishDTO expected = new GameFinishDTO(true, false, false, true);
//        GameProcessDTO gameProcessDTO = new GameProcessDTO();
//        gameProcessDTO.setAfterMoveBoardState(board);
//        gameProcessDTO.setSide(Side.WHITE);
//
//        GameFinishDTO result = gameFinishDetector.checkResultBasedOnBoard(gameProcessDTO);
//
//        assertEquals(expected, result);
//    }
//
//    @Test
//    public void checkResultBasedOnBoard_ShouldBeDraw() {
//        String board = "8/8/8/8/6k1/8/4K3/8 w - - 0 58";
//        GameFinishDTO expected = new GameFinishDTO(false, false, true, true);
//        GameProcessDTO gameProcessDTO = new GameProcessDTO();
//        gameProcessDTO.setAfterMoveBoardState(board);
//        gameProcessDTO.setSide(Side.WHITE);
//
//        GameFinishDTO result = gameFinishDetector.checkResultBasedOnBoard(gameProcessDTO);
//
//        assertEquals(expected, result);
//    }
//
//    @Test
//    public void checkResultBasedOnTime_WhenPlayersHaveGameTime_ShouldGameNotBeFinished() {
//        String board = "rnbqkbnr/pppp1ppp/8/4p3/3P4/8/PPP1PPPP/RNBQKBNR w KQkq e6 0 2";
//        GameFinishDTO expected = new GameFinishDTO(false, false, false, false);
//        GameProcessDTO gameProcessDTO = new GameProcessDTO();
//        gameProcessDTO.setWhiteTime(100L);
//        gameProcessDTO.setBlackTime(100L);
//        gameProcessDTO.setAfterMoveBoardState(board);
//
//        GameFinishDTO result = gameFinishDetector.checkResultBasedOnTime(gameProcessDTO);
//
//        assertEquals(expected, result);
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {"8/8/8/8/8/k7/5K2/8 b - - 0 29", "1k1b4/8/8/8/8/8/8/4K3 w - - 0 1",
//            "1k6/2n5/8/8/8/8/8/4K3 w HAha - 0 1", "1k6/1nn5/8/8/8/8/8/4K3 w - - 0 1"})
//    public void checkResultBasedOnTime_WhenWhitePlayerRunOutOfTime_ShouldBeDraw(String board) {
//        GameFinishDTO expected = new GameFinishDTO(false, false, true, true);
//        GameProcessDTO gameProcessDTO = new GameProcessDTO();
//        gameProcessDTO.setWhiteTime(-2L);
//        gameProcessDTO.setBlackTime(100L);
//        gameProcessDTO.setAfterMoveBoardState(board);
//
//
//        GameFinishDTO result = gameFinishDetector.checkResultBasedOnTime(gameProcessDTO);
//
//        assertEquals(expected, result);
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {"8/8/8/8/8/k7/3K2Pr/8 w - - 0 27", "8/8/8/8/8/k3K3/5r2/8 b - - 3 29",
//            "1k6/1nn5/8/8/8/8/4P3/4K3 w - - 0 1"})
//    public void checkResultBasedOnTime_WhenWhitePlayerRunOutOfTime_ShouldBlackWin(String board) {
//        GameFinishDTO expected = new GameFinishDTO(false, true, false, true);
//        GameProcessDTO gameProcessDTO = new GameProcessDTO();
//        gameProcessDTO.setWhiteTime(-2L);
//        gameProcessDTO.setBlackTime(100L);
//        gameProcessDTO.setAfterMoveBoardState(board);
//
//        GameFinishDTO result = gameFinishDetector.checkResultBasedOnTime(gameProcessDTO);
//
//        assertEquals(expected, result);
//    }
//
//    @Test
//    public void checkResultBasedOnTime_WhenWhitePlayerRunOutOfTime_ShouldWhiteWin() {
////        String board = "8/8/8/8/8/k7/3K2Pr/8 w - - 0 27";
////        GameFinishDTO expected = new GameFinishDTO(false, true, false, true);
////        GameProcessDTO gameProcessDTO = new GameProcessDTO();
////        gameProcessDTO.setWhiteTime(-2L);
////        gameProcessDTO.setBlackTime(100L);
////        gameProcessDTO.setAfterMoveBoardState(board);
////
////        GameFinishDTO result = gameFinishDetector.checkResultBasedOnTime(gameProcessDTO);
////
////        assertEquals(expected, result);
//    }


}