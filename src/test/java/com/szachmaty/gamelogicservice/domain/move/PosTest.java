package com.szachmaty.gamelogicservice.domain.move;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PosTest {

    @Test
    void givenAllBoardValidPositionsConsistingOfOnlyNumbers_whenTestValid_thenNumberOfValidPositionsEqual64() {
        // given
        final var expectedSize = 64;
        var poses = new ArrayList<Pos>();

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                poses.add(Pos.of(i, j));
            }
        }

        // when
        var validPoses = poses.stream()
                .filter(Pos::isPosValid)
                .toList();

        // then
        assertEquals(expectedSize, validPoses.size());
    }

    @Test
    void givenAllBoardValidPositionsConsistingOfNumbersAndCharacters_whenTestValid_thenNumberOfValidPositionsEqual64() {
        // given
        final var expectedSize = 64;
        final var files = "abcdefgh";
        var poses = new ArrayList<Pos>();

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                var file = String.valueOf(files.charAt(i - 1));
                poses.add(Pos.of(file  + j));
            }
        }

        // when
        var validPoses = poses.stream()
                .filter(Pos::isPosValid)
                .toList();

        // then
        assertEquals(expectedSize, validPoses.size());
    }

}