package com.miladjafari.mancala.service;

import com.miladjafari.mancala.repository.GameEngineRepository;
import com.miladjafari.mancala.sdk.exception.GameEngineStarterException;
import com.miladjafari.mancala.sdk.gameengine.GameEngine;
import com.miladjafari.mancala.sdk.gameengine.GameEngineStarter;
import com.miladjafari.mancala.websocket.GameNotifier;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@QuarkusTest
class GameManagerServiceTest {

    protected GameEngine gameEngine;

    @Inject
    GameManagerService gameManagerService;

    @InjectMock
    GameEngineRepository gameEngineRepository;

    @InjectMock
    GameNotifier gameNotifier;

    @Test
    public void testPlayAndFireBoardChangeEvent() throws GameEngineStarterException {
        final Integer PIT_INDEX = 6;
        final String GAME_ID = UUID.randomUUID().toString();
        final String PLAYER1 = "Milad";
        final String PLAYER2 = "Michael";

        gameEngine = new GameEngineStarter()
                .gameId(GAME_ID)
                .addPlayer(PLAYER1)
                .addPlayer(PLAYER2)
                .start();

        Mockito.when(gameEngineRepository.findById(anyString())).thenReturn(Optional.ofNullable(gameEngine));
        gameManagerService.play("mockeGameId", PLAYER1, PIT_INDEX);

        verify(gameNotifier, times(1)).onBoardChange(any());
    }

    @Test
    public void testGamingOver() throws GameEngineStarterException {
        final Integer PIT_INDEX = 6;
        final String GAME_ID = UUID.randomUUID().toString();
        final String PLAYER1 = "Milad";
        final String PLAYER2 = "Michael";

        gameEngine = new GameEngineStarter()
                .gameId(GAME_ID)
                .addPlayer(PLAYER1)
                .addPlayer(PLAYER2)
                .onGameOver(gameInfo -> {
                    assertEquals(GAME_ID, gameInfo.getGameId());
                    assertEquals(PLAYER1, gameInfo.getWinner());
                    assertTrue(gameInfo.isGameOver());
                })
                .start();

        gameEngine.getPlayers().get(PLAYER1).getPit(1).pickUpStones();
        gameEngine.getPlayers().get(PLAYER1).getPit(2).pickUpStones();
        gameEngine.getPlayers().get(PLAYER1).getPit(3).pickUpStones();
        gameEngine.getPlayers().get(PLAYER1).getPit(4).pickUpStones();
        gameEngine.getPlayers().get(PLAYER1).getPit(5).pickUpStones();

        Mockito.when(gameEngineRepository.findById(anyString())).thenReturn(Optional.ofNullable(gameEngine));
        gameManagerService.play("mockeGameId", PLAYER1, PIT_INDEX);

        verifyNoMoreInteractions(gameNotifier);
    }
}