package com.miladjafari.mancala.gamesdk.gameengine;

import com.miladjafari.mancala.sdk.exception.GameEngineException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameEngineBoardStatusTest extends AbstractGameEngineTest {

    @Test
    public void testSuccessGetBoardStatusFromPlayerPointOfView() throws GameEngineException {
        final Integer PIT_INDEX = 1;
        Map<String, Integer> expectedPlayerBoardStatus = new HashMap<>() {{
            //player playground
            put("1", 0);
            put("2", 7);
            put("3", 7);
            put("4", 7);
            put("5", 7);
            put("6", 7);
            put("7", 1);

            //opponent playground
            put("8", 6);
            put("9", 6);
            put("10", 6);
            put("11", 6);
            put("12", 6);
            put("13", 6);
            put("14", 0);
        }};
        gameEngine.play(player1, PIT_INDEX);

        Map<String, Integer> actualPlayerBoardStatus = gameEngine.getBoardStatusOf(player1);
        assertEquals(expectedPlayerBoardStatus, actualPlayerBoardStatus);
    }

    @Test
    public void testSuccessGetBoardStatusFromOpponentPointOfView() throws GameEngineException {
        final Integer PIT_INDEX = 1;
        Map<String, Integer> expectedOpponentBoardStatus = new HashMap<>() {{
            //opponent playground
            put("1", 6);
            put("2", 6);
            put("3", 6);
            put("4", 6);
            put("5", 6);
            put("6", 6);
            put("7", 0);

            //player playground
            put("8", 0);
            put("9", 7);
            put("10", 7);
            put("11", 7);
            put("12", 7);
            put("13", 7);
            put("14", 1);
        }};
        gameEngine.play(player1, PIT_INDEX);
        String opponentPlayer = gameEngine.findOpponentBy(player1);

        Map<String, Integer> actualOpponentBoardStatus = gameEngine.getBoardStatusOf(opponentPlayer);
        assertEquals(expectedOpponentBoardStatus, actualOpponentBoardStatus);
    }

    @Test
    public void testSuccessGetBoardStatus() throws GameEngineException {
        final Integer PIT_INDEX = 1;
        Map<String, Integer> expectedPlayerBoardStatus = new HashMap<>() {{
            //player playground
            put("1", 0);
            put("2", 7);
            put("3", 7);
            put("4", 7);
            put("5", 7);
            put("6", 7);
            put("7", 1);

            //opponent playground
            put("8", 6);
            put("9", 6);
            put("10", 6);
            put("11", 6);
            put("12", 6);
            put("13", 6);
            put("14", 0);
        }};

        Map<String, Integer> expectedOpponentBoardStatus = new HashMap<>() {{
            //opponent playground
            put("1", 6);
            put("2", 6);
            put("3", 6);
            put("4", 6);
            put("5", 6);
            put("6", 6);
            put("7", 0);

            //player playground
            put("8", 0);
            put("9", 7);
            put("10", 7);
            put("11", 7);
            put("12", 7);
            put("13", 7);
            put("14", 1);
        }};
        gameEngine.play(player1, PIT_INDEX);
        String opponentPlayer = gameEngine.findOpponentBy(player1);

        Map<String, Integer> actualPlayerBoardStatus = gameEngine.getBoardStatusOf(player1);
        Map<String, Integer> actualOpponentBoardStatus = gameEngine.getBoardStatusOf(opponentPlayer);

        assertEquals(expectedPlayerBoardStatus, actualPlayerBoardStatus);
        assertEquals(expectedOpponentBoardStatus, actualOpponentBoardStatus);
    }
}