package com.miladjafari.mancala.gamesdk.gameengine;

import com.miladjafari.mancala.sdk.Stone;
import com.miladjafari.mancala.sdk.exception.GameEngineException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameEnginePlaygroundTest extends AbstractGameEngineTest {

    @Test
    public void testSuccessMoveFromPitIndexOne() throws GameEngineException {
        final Integer PIT_INDEX = 1;
        expectedPlayers.get(player1).getPit(1).pickUpStones();
        expectedPlayers.get(player1).getPit(2).pushStone(new Stone());
        expectedPlayers.get(player1).getPit(3).pushStone(new Stone());
        expectedPlayers.get(player1).getPit(4).pushStone(new Stone());
        expectedPlayers.get(player1).getPit(5).pushStone(new Stone());
        expectedPlayers.get(player1).getPit(6).pushStone(new Stone());
        expectedPlayers.get(player1).getPit(7).pushStone(new Stone());

        gameEngine.play(player1, PIT_INDEX);

        assertPlayersPlayground(expectedPlayers, gameEngine.getPlayers());
    }

    @Test
    public void testSuccessMoveFromPitIndexSix() throws GameEngineException {
        final Integer PIT_INDEX = 6;
        expectedPlayers.get(player1).getPit(PIT_INDEX).pickUpStones();
        expectedPlayers.get(player1).getPit(7).pushStone(new Stone());

        expectedPlayers.get(player2).getPit(6).pushStone(new Stone());
        expectedPlayers.get(player2).getPit(5).pushStone(new Stone());
        expectedPlayers.get(player2).getPit(4).pushStone(new Stone());
        expectedPlayers.get(player2).getPit(3).pushStone(new Stone());
        expectedPlayers.get(player2).getPit(2).pushStone(new Stone());

        gameEngine.play(player1, PIT_INDEX);

        assertPlayersPlayground(expectedPlayers, gameEngine.getPlayers());
    }

    @Test
    public void testSuccessMoveFromPitIndexSixWithEightStoneAndPlayInPlayerAndOpponentPlaygroundInCircular()
            throws GameEngineException {

        final Integer ADD_TWO_EXTRA_STONE_IN_PIT = 2;
        final Integer PIT_INDEX_SIX = 6;

        initPlayerPlaygroundBeforeTest(player1, PIT_INDEX_SIX, ADD_TWO_EXTRA_STONE_IN_PIT);

        expectedPlayers.get(player1).getPit(PIT_INDEX_SIX).pickUpStones();
        expectedPlayers.get(player1).getPit(1).pushStone(new Stone());
        expectedPlayers.get(player1).getPit(7).pushStone(new Stone());

        expectedPlayers.get(player2).getPit(6).pushStone(new Stone());
        expectedPlayers.get(player2).getPit(5).pushStone(new Stone());
        expectedPlayers.get(player2).getPit(4).pushStone(new Stone());
        expectedPlayers.get(player2).getPit(3).pushStone(new Stone());
        expectedPlayers.get(player2).getPit(2).pushStone(new Stone());
        expectedPlayers.get(player2).getPit(1).pushStone(new Stone());

        gameEngine.play(player1, PIT_INDEX_SIX);

        assertPlayersPlayground(expectedPlayers, gameEngine.getPlayers());
    }

    @Test
    public void testFailMoveFromBigPit() {
        final Integer BIG_PIT_INDEX = 7;
        assertThrows(GameEngineException.class, () -> gameEngine.play(player1, BIG_PIT_INDEX));

    }

    @Test
    public void testFailMoveFromEmptyPit() {
        final Integer EMPTY_PIT_INDEX = 1;
        expectedPlayers.get(player1).getPit(EMPTY_PIT_INDEX).pickUpStones();

        gameEngine.getPlayers().get(player1).getPit(EMPTY_PIT_INDEX).pickUpStones();
        assertThrows(GameEngineException.class, () -> gameEngine.play(player1, EMPTY_PIT_INDEX));
    }

    @Test
    public void testSuccessMoveByPlayer1Pit5ThenPlayer2Pit3AndThenPlayer1Pit6()
            throws GameEngineException {

        final Integer PIT_INDEX_TREE = 3;
        final Integer PIT_INDEX_FIVE = 5;
        final Integer PIT_INDEX_SIX = 6;

        expectedPlayers.get(player1).getPit(PIT_INDEX_FIVE).pickUpStones();
        expectedPlayers.get(player1).getPit(6).pushStone(new Stone());
        expectedPlayers.get(player1).getPit(7).pushStone(new Stone());

        expectedPlayers.get(player2).getPit(6).pushStone(new Stone());
        expectedPlayers.get(player2).getPit(5).pushStone(new Stone());
        expectedPlayers.get(player2).getPit(4).pushStone(new Stone());
        expectedPlayers.get(player2).getPit(3).pushStone(new Stone());

        gameEngine.play(player1, PIT_INDEX_FIVE);
        assertPlayersPlayground(expectedPlayers, gameEngine.getPlayers());

        expectedPlayers.get(player2).getPit(PIT_INDEX_TREE).pickUpStones();
        expectedPlayers.get(player2).getPit(4).pushStone(new Stone());
        expectedPlayers.get(player2).getPit(5).pushStone(new Stone());
        expectedPlayers.get(player2).getPit(6).pushStone(new Stone());
        expectedPlayers.get(player2).getPit(7).pushStone(new Stone());

        expectedPlayers.get(player1).getPit(6).pushStone(new Stone());
        expectedPlayers.get(player1).getPit(5).pushStone(new Stone());
        expectedPlayers.get(player1).getPit(4).pushStone(new Stone());

        gameEngine.play(player2, PIT_INDEX_TREE);
        assertPlayersPlayground(expectedPlayers, gameEngine.getPlayers());

        expectedPlayers.get(player1).getPit(PIT_INDEX_SIX).pickUpStones();
        expectedPlayers.get(player1).getPit(7).pushStone(new Stone());

        expectedPlayers.get(player2).getPit(6).pushStone(new Stone());
        expectedPlayers.get(player2).getPit(5).pushStone(new Stone());
        expectedPlayers.get(player2).getPit(4).pushStone(new Stone());
        expectedPlayers.get(player2).getPit(3).pushStone(new Stone());
        expectedPlayers.get(player2).getPit(2).pushStone(new Stone());
        expectedPlayers.get(player2).getPit(1).pushStone(new Stone());

        expectedPlayers.get(player1).getPit(1).pushStone(new Stone());

        gameEngine.play(player1, PIT_INDEX_SIX);
        assertPlayersPlayground(expectedPlayers, gameEngine.getPlayers());
    }

    @Test
    public void testSuccessGetRewardIfPitIsEmptyAndStoneIsTheLast() throws GameEngineException {
        final Integer PIT_INDEX_ONE = 1;
        final Integer PIT_INDEX_THREE = 3;

        expectedPlayers.get(player1).getPit(PIT_INDEX_ONE).pickUpStones();
        expectedPlayers.get(player1).getPit(2).pushStone(new Stone());

        expectedPlayers.get(player1).getPit(3).pickUpStones();
        expectedPlayers.get(player1).getPit(3).pushStone(new Stone());

        //move all 6 stone from player2 to player1 big pit
        expectedPlayers.get(player1).getPit(7).pushStone(new Stone());
        expectedPlayers.get(player1).getPit(7).pushStone(new Stone());
        expectedPlayers.get(player1).getPit(7).pushStone(new Stone());
        expectedPlayers.get(player1).getPit(7).pushStone(new Stone());
        expectedPlayers.get(player1).getPit(7).pushStone(new Stone());
        expectedPlayers.get(player1).getPit(7).pushStone(new Stone());

        expectedPlayers.get(player2).getPit(PIT_INDEX_THREE).pickUpStones();

        gameEngine.getPlayers().get(player1).getPit(PIT_INDEX_ONE).pickUpStones();
        gameEngine.getPlayers().get(player1).getPit(PIT_INDEX_ONE).pushStone(new Stone());
        gameEngine.getPlayers().get(player1).getPit(PIT_INDEX_ONE).pushStone(new Stone());

        gameEngine.getPlayers().get(player1).getPit(PIT_INDEX_THREE).pickUpStones(); //create an empty pit

        gameEngine.play(player1, PIT_INDEX_ONE);
        assertPlayersPlayground(expectedPlayers, gameEngine.getPlayers());
    }
}