package com.miladjafari.mancala.gamesdk;

import com.miladjafari.mancala.sdk.Playground;
import com.miladjafari.mancala.sdk.exception.GameEngineStarterException;
import com.miladjafari.mancala.sdk.gameengine.GameEngine;
import com.miladjafari.mancala.sdk.gameengine.GameEngineStarter;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.miladjafari.mancala.gamesdk.GameAssert.assertPlayground;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class GameEngineStarterTest {
    private static final Integer SIX_PITS = 6;
    private static final Integer SIX_STONES_IN_EACH_PITS = 6;

    private static void assertPlayers(
            Map<String, Playground> expectedPlayers,
            Map<String, Playground> actualPitsPlayers) {

        assertEquals(expectedPlayers.size(), actualPitsPlayers.size());

        expectedPlayers.forEach(((expectedPlayer, expectedPlayground) -> {
            Playground actualPlayground = actualPitsPlayers.get(expectedPlayer);
            String actualPlayer = findPlayer(actualPitsPlayers.keySet(), expectedPlayer);

            assertEquals(expectedPlayer, actualPlayer);
            assertPlayground(expectedPlayground, actualPlayground);
        }));
    }

    private static String findPlayer(Set<String> players, String targetPlayer) {
        return players.stream().filter(player -> player.equals(targetPlayer)).collect(Collectors.toList()).get(0);
    }

    @Test
    public void testSuccessStartEngineIfEngineHasTwoPlayers() throws GameEngineStarterException {
        Map<String, Playground> expectedPlayers = createMockPlayers();

        String player1 = "Milad";
        String player2 = "Michael";

        GameEngine gameEngine = new GameEngineStarter()
                .addPlayer(player1)
                .addPlayer(player2)
                .start();

        assertNotNull(gameEngine);

        Map<String, Playground> actualPlayers = gameEngine.getPlayers();
        assertPlayers(expectedPlayers, actualPlayers);
    }

    @Test
    public void testFailStartEngineIfMoreThanTwoPlayerIsAddedToEngine() {
        String player1 = "Milad";
        String player2 = "Michael";
        String player3 = "Mihaela";

        assertThrows(IllegalArgumentException.class, () ->
                new GameEngineStarter()
                        .addPlayer(player1)
                        .addPlayer(player2)
                        .addPlayer(player3)
                        .start()
        );
    }

    @Test
    public void testFailStartEngineIfLessThanTwoPlayerIsAddedToEngine() {
        String player1 = "Milad";

        assertThrows(GameEngineStarterException.class, () -> new GameEngineStarter().addPlayer(player1).start());
    }

    @Test
    public void testFailStartEngineIfPlayerIsDuplicated() {
        String player1 = "Milad";
        assertThrows(IllegalArgumentException.class, () ->
                new GameEngineStarter()
                        .addPlayer(player1)
                        .addPlayer(player1)
                        .start()
        );
    }

    @Test
    public void testFailStartEngineIfGameIdIsNotSet() {
        String player1 = "Milad";
        String player2 = "Michael";

        assertThrows(GameEngineStarterException.class, () ->
                new GameEngineStarter()
                        .gameId(null)
                        .addPlayer(player1)
                        .addPlayer(player2)
                        .start()
        );
    }

    private Map<String, Playground> createMockPlayers() {
        Map<String, Playground> players = new TreeMap<>();

        String player1 = "Milad";
        String player2 = "Michael";

        players.put(player1, createMockPlayground());
        players.put(player2, createMockPlayground());

        return players;
    }

    private Playground createMockPlayground() {
        return Playground.builder()
                         .numberOfPits(SIX_PITS)
                         .numberOfStonesInEachPit(SIX_STONES_IN_EACH_PITS)
                         .build();
    }
}