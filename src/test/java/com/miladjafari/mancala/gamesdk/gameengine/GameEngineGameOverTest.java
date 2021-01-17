package com.miladjafari.mancala.gamesdk.gameengine;

import com.miladjafari.mancala.sdk.Stone;
import com.miladjafari.mancala.sdk.exception.GameEngineException;
import com.miladjafari.mancala.sdk.exception.GameEngineStarterException;
import com.miladjafari.mancala.sdk.gameengine.GameEngineStarter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameEngineGameOverTest extends AbstractGameEngineTest {

    @BeforeEach
    @Override
    public void beforeTest() throws GameEngineStarterException {
        super.beforeTest();
        gameEngine = new GameEngineStarter()
                .addPlayer(player1)
                .addPlayer(player2)
                .onGameOver(players -> {
                    System.out.println("[TEST] GAME IS OVER!!! ");
                    players.forEach((player, playground) ->
                                            System.out.printf("[TEST] %s: %d \n", player.getName(),
                                                              playground.getBigPit().getCountOfStones()));
                })
                .start();
    }

    @Test
    public void testSuccessGameOverIfAllPitsOfPlayer1IsEmpty() throws GameEngineException {
        final Integer PIT_INDEX = 6;
        expectedPlayers.get(player1).getPit(1).pickUpStones();
        expectedPlayers.get(player1).getPit(2).pickUpStones();
        expectedPlayers.get(player1).getPit(3).pickUpStones();
        expectedPlayers.get(player1).getPit(4).pickUpStones();
        expectedPlayers.get(player1).getPit(5).pickUpStones();
        expectedPlayers.get(player1).getPit(6).pickUpStones();
        expectedPlayers.get(player1).getPit(7).pushStone(new Stone());

        expectedPlayers.get(player2).getPit(6).pushStone(new Stone());
        expectedPlayers.get(player2).getPit(5).pushStone(new Stone());
        expectedPlayers.get(player2).getPit(4).pushStone(new Stone());
        expectedPlayers.get(player2).getPit(3).pushStone(new Stone());
        expectedPlayers.get(player2).getPit(2).pushStone(new Stone());

        gameEngine.getPlayers().get(player1).getPit(1).pickUpStones();
        gameEngine.getPlayers().get(player1).getPit(2).pickUpStones();
        gameEngine.getPlayers().get(player1).getPit(3).pickUpStones();
        gameEngine.getPlayers().get(player1).getPit(4).pickUpStones();
        gameEngine.getPlayers().get(player1).getPit(5).pickUpStones();

        gameEngine.play(player1, PIT_INDEX);
        assertPlayersPlayground(expectedPlayers, gameEngine.getPlayers());
    }

    @Test
    public void testFailPlayIfGameWasOver() throws GameEngineException {
        final Integer PIT_INDEX = 6;

        gameEngine.getPlayers().get(player1).getPit(1).pickUpStones();
        gameEngine.getPlayers().get(player1).getPit(2).pickUpStones();
        gameEngine.getPlayers().get(player1).getPit(3).pickUpStones();
        gameEngine.getPlayers().get(player1).getPit(4).pickUpStones();
        gameEngine.getPlayers().get(player1).getPit(5).pickUpStones();

        gameEngine.play(player1, PIT_INDEX);
        assertThrows(GameEngineException.class, () -> gameEngine.play(player2, PIT_INDEX));
    }
}