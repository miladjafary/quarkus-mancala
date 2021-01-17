package com.miladjafari.mancala.gamesdk;

import com.miladjafari.mancala.sdk.Player;
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
            Map<Player, Playground> expectedPlayers,
            Map<Player, Playground> actualPitsPlayers) {

        assertEquals(expectedPlayers.size(), actualPitsPlayers.size());

        expectedPlayers.forEach(((expectedPlayer, expectedPlayground) -> {
            Playground actualPlayground = actualPitsPlayers.get(expectedPlayer);
            Player actualPlayer = findPlayer(actualPitsPlayers.keySet(), expectedPlayer);

            assertEquals(expectedPlayer, actualPlayer);
            assertPlayground(expectedPlayground, actualPlayground);
        }));
    }

    private static Player findPlayer(Set<Player> players, Player targetPlayer) {
        return players.stream().filter(player -> player.equals(targetPlayer)).collect(Collectors.toList()).get(0);
    }

    @Test
    public void testSuccessStartEngineIfEngineHasTwoPlayers() throws GameEngineStarterException {
        Map<Player, Playground> expectedPlayers = createMockPlayers();

        Player player1 = new Player("Milad");
        Player player2 = new Player("Michael");

        GameEngine gameEngine = new GameEngineStarter()
                .addPlayer(player1)
                .addPlayer(player2)
                .start();

        assertNotNull(gameEngine);

        Map<Player, Playground> actualPlayers = gameEngine.getPlayers();
        assertPlayers(expectedPlayers, actualPlayers);
    }

    @Test
    public void testFailStartEngineIfMoreThanTwoPlayerIsAddedToEngine() throws GameEngineStarterException {
        Player player1 = new Player("Milad");
        Player player2 = new Player("Michael");
        Player player3 = new Player("Mihaela");

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
        Player player1 = new Player("Milad");

        assertThrows(GameEngineStarterException.class, () -> new GameEngineStarter().addPlayer(player1).start());
    }

    @Test
    public void testFailStartEngineIfPlayerIsDuplicated() {
        Player player1 = new Player("Milad");
        assertThrows(IllegalArgumentException.class, () ->
                new GameEngineStarter()
                        .addPlayer(player1)
                        .addPlayer(player1)
                        .start()
        );
    }

    private Map<Player, Playground> createMockPlayers() {
        Map<Player, Playground> players = new TreeMap<>();

        Player player1 = new Player("Milad");
        Player player2 = new Player("Michael");

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