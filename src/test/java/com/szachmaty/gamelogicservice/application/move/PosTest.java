package com.szachmaty.gamelogicservice.application.move;

import com.szachmaty.gamelogicservice.application.board.BoardState;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PosTest {

    @Test
    void givenAllBoardValidPositionsConsistingOfOnlyNumbers_whenTestIfValid_thenNumberOfValidPositionsEqual64() {
        // given
        final var expectedSize = 64;
        var positions = new ArrayList<Pos>();

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                positions.add(Pos.of(i, j));
            }
        }

        // when
        var validPositions = positions.stream()
                .filter(BoardState::isPosValid)
                .toList();

        // then
        assertEquals(expectedSize, validPositions.size());
    }

    @Test
    void givenAllBoardValidPositionsConsistingOfNumbersAndCharacters_whenTestIfValid_thenNumberOfValidPositionsEqual64() {
        // given
        final var expectedSize = 64;
        final var files = "abcdefgh";
        var positions = new ArrayList<Pos>();

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                var file = String.valueOf(files.charAt(i - 1));
                positions.add(Pos.of(file  + j));
            }
        }

        // when
        var validPositions = positions.stream()
                .filter(BoardState::isPosValid)
                .toList();

        // then
        assertEquals(expectedSize, validPositions.size());
    }

    @Test
    void givenIllegalBoardPositionConsistingOfNumbers_whenTestIfValid_thenNoneIsValid() {
        // given
        final var expectedSize = 0;
        var positions = new ArrayList<Pos>();

        for (int i = -2; i <= 10; i++) {
            if (i >= 1 && i <= 8) {
                continue;
            }
            for (int j = -2; j <= 10; j++) {
                if (j >= 1 && j <= 8) {
                    continue;
                }
                positions.add(Pos.of(i, j));
            }
        }

        // when
        var validPositions = positions.stream()
                .filter(BoardState::isPosValid)
                .toList();

        assertEquals(expectedSize, validPositions.size());
    }

}