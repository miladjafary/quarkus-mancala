package com.miladjafari.mancala.gamesdk.gameengine;

import com.miladjafari.mancala.sdk.exception.GameEngineException;
import org.junit.jupiter.api.Test;

import static com.miladjafari.mancala.gamesdk.GameAssert.assertPlayer;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameEngineTurnTest extends AbstractGameEngineTest {

    @Test
    public void testNextTurnIsCurrentPlayerIfMoveIsStartedFromPitIndexOne() throws GameEngineException {
        final Integer PIT_INDEX = 1;

        gameEngine.play(player1, PIT_INDEX);
        assertPlayer(player1, gameEngine.getNextTurn());
    }

    @Test
    public void testNextTurnIsOpponentPlayerIfMoveIsStartedFromPitIndexSix() throws GameEngineException {
        final Integer PIT_INDEX = 6;

        gameEngine.play(player1, PIT_INDEX);
        assertPlayer(player2, gameEngine.getNextTurn());
    }

    @Test
    public void testSuccessMoveFromPitIndexFiveWithFifteenStoneAndPlayInPlayerAndOpponentPlaygroundInCircular()
            throws GameEngineException {

        final Integer ADD_NINE_EXTRA_STONE_IN_PIT = 9;
        final Integer PIT_INDEX_FIVE = 5;

        initPlayerPlaygroundBeforeTest(player1, PIT_INDEX_FIVE, ADD_NINE_EXTRA_STONE_IN_PIT);

        gameEngine.play(player1, PIT_INDEX_FIVE);
        assertPlayer(player1, gameEngine.getNextTurn());
    }

    @Test
    public void testTurnIfPlayer1TurnThenPlayer2TurnAndThenPlayer1Turn() throws GameEngineException {
        final Integer PIT_INDEX = 3;
        gameEngine.play(player1, PIT_INDEX);
        assertPlayer(player2, gameEngine.getNextTurn());

        gameEngine.play(player2, PIT_INDEX);
        assertPlayer(player1, gameEngine.getNextTurn());

        gameEngine.play(player1, PIT_INDEX + 1);
        assertPlayer(player2, gameEngine.getNextTurn());
    }

    @Test
    public void testFailPlayIfItIsThePlayer1TurnButPlayer2PlayTheGame() {
        final Integer PIT_INDEX = 1;
        String playByPlayerWhoAreNotHaveTurn = player2;

        assertThrows(GameEngineException.class, () -> gameEngine.play(playByPlayerWhoAreNotHaveTurn, PIT_INDEX));
    }
}