package com.miladjafari.mancala.gamesdk.gameengine;

import com.miladjafari.mancala.sdk.Pit;
import com.miladjafari.mancala.sdk.Player;
import com.miladjafari.mancala.sdk.Playground;
import com.miladjafari.mancala.sdk.Stone;
import com.miladjafari.mancala.sdk.exception.GameEngineStarterException;
import com.miladjafari.mancala.sdk.gameengine.GameEngine;
import com.miladjafari.mancala.sdk.gameengine.GameEngineStarter;
import org.junit.jupiter.api.BeforeEach;


import java.util.HashMap;
import java.util.Map;

import static com.miladjafari.mancala.gamesdk.GameAssert.assertPlayground;

public class AbstractGameEngineTest {

    protected static final Integer SIX_PITS = 6;
    protected static final Integer SIX_STONES_IN_EACH_PITS = 6;

    protected Map<Player, Playground> expectedPlayers;

    protected Player player1;
    protected Player player2;

    protected GameEngine gameEngine;

    @BeforeEach
    public void beforeTest() throws GameEngineStarterException {
        expectedPlayers = createBaseMockPlayers();

        player1 = new Player("Milad");
        player2 = new Player("Michael");

        gameEngine = new GameEngineStarter()
                .addPlayer(player1)
                .addPlayer(player2)
                .start();

    }

    protected void assertPlayersPlayground(
            Map<Player, Playground> expectedPlayersPlayground,
            Map<Player, Playground> actualPlayersPlayground
    ) {
        Playground expectedPlayer1Playground = expectedPlayersPlayground.get(player1);
        Playground expectedPlayer2Playground = expectedPlayersPlayground.get(player2);

        Playground actualPlayer1Playground = actualPlayersPlayground.get(player1);
        Playground actualPlayer2Playground = actualPlayersPlayground.get(player2);

        assertPlayground(expectedPlayer1Playground, actualPlayer1Playground);
        assertPlayground(expectedPlayer2Playground, actualPlayer2Playground);
    }

    protected void initPlayerPlaygroundBeforeTest(Player player, Integer pitIndex, Integer numberOfStones) {
        Pit pit = gameEngine.getPlayers().get(player).getPit(pitIndex);

        for (int stone=0;stone<numberOfStones;stone++) {
            pit.pushStone(new Stone());
        }
    }

    protected Map<Player, Playground> createBaseMockPlayers() {
        Playground playground1 = Playground.builder().numberOfPits(SIX_PITS).numberOfStonesInEachPit(SIX_STONES_IN_EACH_PITS).build();
        Playground playground2 = Playground.builder().numberOfPits(SIX_PITS).numberOfStonesInEachPit(SIX_STONES_IN_EACH_PITS).build();

        Player player1 = new Player("Milad");
        Player player2 = new Player("Michael");

        Map<Player, Playground> players = new HashMap<>();
        players.put(player1, playground1);
        players.put(player2, playground2);

        return players;
    }

}