package com.szachmaty.gamelogicservice.infrastructure.persistence.repository;

import com.szachmaty.gamelogicservice.domain.game.Game;
import com.szachmaty.gamelogicservice.domain.move.Move;
import com.szachmaty.gamelogicservice.infrastructure.persistence.entity.GameEntity;
import com.szachmaty.gamelogicservice.infrastructure.persistence.entity.MoveEntity;
import com.szachmaty.gamelogicservice.infrastructure.persistence.entity.UserEntity;
import com.szachmaty.gamelogicservice.infrastructure.persistence.entity.enumeration.GameStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.szachmaty.gamelogicservice.infrastructure.persistence.entity.enumeration.GameStatus.NOT_STARTED;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameEntityRepositoryTest {

    @Autowired
    public GameEntityRepository gameEntityRepository;
    @BeforeEach
    public void setup() {
        propertiesSetup(1L, null, null,
                null, null , NOT_STARTED, null);
        propertiesSetup(2L, new UserEntity(1L , "Marcin"), new UserEntity(2L, "Łukasz"),
                null, null , NOT_STARTED, null);
        List<MoveEntity> moves = List.of(new MoveEntity(1L,"Nf3"), new MoveEntity(2L, "e4e5"),
                new MoveEntity(3L, "Bb4+"));
        propertiesSetup(3L, null, null,
                null, null , NOT_STARTED, moves);
    }


    @Test
    public void findById_WhenMostsGameEntityFieldsAreNull_ShouldBeSuccesful() {
        long expectedGameId = 1L;


        Optional<GameEntity> optionalGameEntity = gameEntityRepository.findById(expectedGameId);

        assertNotNull(optionalGameEntity);
        assertEquals(expectedGameId, optionalGameEntity.get().getGameId());
    }
    @Test
    public void findById_WhenUsersExists_ShouldBeSuccesful() {
        long expectedGameId = 2L;
        String expectedWhiteUserName = "Marcin";
        String expectedBlackUserName = "Łukasz";

        Optional<GameEntity> optionalGameEntity = gameEntityRepository.findById(expectedGameId);

        assertNotNull(optionalGameEntity);
        assertNotNull(optionalGameEntity.get().getWhiteUser());
        assertNotNull(optionalGameEntity.get().getBlackUser());
        assertEquals(expectedGameId, optionalGameEntity.get().getGameId());
        assertEquals(expectedWhiteUserName, optionalGameEntity.get().getWhiteUser().getUsername());
        assertEquals(expectedBlackUserName, optionalGameEntity.get().getBlackUser().getUsername());
    }
    @Test
    public void findById_WhenMovesExists_ShouldBeSuccesful() {
        long expectedGameId = 3L;
        String expectedFirstMove = "Nf3";
        String expectedThirdMove = "Bb4+";

        Optional<GameEntity> optionalGameEntity = gameEntityRepository.findById(expectedGameId);

        assertNotNull(optionalGameEntity);
        assertNotNull(optionalGameEntity.get().getMoveList());
        assertEquals(expectedGameId, optionalGameEntity.get().getGameId());
        assertEquals(expectedFirstMove, optionalGameEntity.get().getMoveList().get(0).getMove());
        assertEquals(expectedThirdMove, optionalGameEntity.get().getMoveList().get(2).getMove());

    }
//    @ParameterizedTest
//    @MethodSource("provideGameEntities")
//    public void findAll_WhenBaseTestCase_ShouldBeSuccesful() {
//
//    }
//    public Stream<GameEntity> provideGameEntities() {
//        GameEntity gameEntity = GameEntity.builder()
//                .gameId(1L)
//                .whiteUser(null)
//                .blackUser(null)
//                .whiteTime(null)
//                .blackTime(null)
//                .gameStatus(NOT_STARTED)
//                .moveList(null)
//                .build();
//        gameEntityRepository.save(gameEntity);
//        return Stream.of(gameEntity);
//    }
    private GameEntity propertiesSetup(
            long gameId, UserEntity whiteUser, UserEntity blackUser, LocalDateTime whiteLocalDateTime,
            LocalDateTime blackLocalDateTime, GameStatus gameStatus, List<MoveEntity> moveList
    ) {
        GameEntity gameEntity = GameEntity.builder()
                .gameId(gameId)
                .whiteUser(whiteUser)
                .blackUser(blackUser)
                .whiteTime(whiteLocalDateTime)
                .blackTime(blackLocalDateTime)
                .gameStatus(gameStatus)
                .moveList(moveList)
                .build();
        gameEntityRepository.save(gameEntity);
        return gameEntity;
    }
}