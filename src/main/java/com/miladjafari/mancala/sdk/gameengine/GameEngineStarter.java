package com.miladjafari.mancala.sdk.gameengine;

import com.miladjafari.mancala.sdk.Player;
import com.miladjafari.mancala.sdk.Playground;
import com.miladjafari.mancala.sdk.exception.GameEngineStarterException;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Consumer;

public class GameEngineStarter {
    private static final Integer MAX_PLAYERS = 2;
    private static final Integer NUMBER_OF_PITS = 6;
    private static final Integer NUMBER_OF_STONE_IN_EACH_PIT = 6;

    private Map<Player, Playground> players = new TreeMap<>();
    private Player firstTurn;
    private Consumer<Map<Player, Playground>> onGameOver = players -> {
        System.out.println("GAME IS OVER !!!");
        players.forEach((player, playground) -> {
            System.out.println(String.format("Player %s: %02d stones",
                    player.getName(),
                    playground.getBigPit().getCountOfStones()));
        });
    };

    public GameEngineStarter addPlayer(Player player) {
        if (hasTwoPlayers()) {
            throw new IllegalArgumentException(String.format("Maximum number of players is %s.", MAX_PLAYERS));
        }

        if (isExist(player)) {
            throw new IllegalArgumentException(String.format("Player \"%s\" is exist.", player.getName()));
        }

        Playground playground = createPlayground();
        players.put(player, playground);
        assignFirstTurnToPlayerIfFirstTurnIsNull(player);

        return this;
    }

    public GameEngineStarter onGameOver(Consumer<Map<Player, Playground>> onGameOver) {
        this.onGameOver = onGameOver;
        return this;
    }

    private void assignFirstTurnToPlayerIfFirstTurnIsNull(Player player) {
        firstTurn = Optional.ofNullable(firstTurn).orElse(player);
    }

    private Playground createPlayground() {
        return Playground.builder()
                .numberOfPits(NUMBER_OF_PITS)
                .numberOfStonesInEachPit(NUMBER_OF_STONE_IN_EACH_PIT)
                .build();
    }

    private Boolean isExist(Player player) {
        return players.containsKey(player);
    }

    public Boolean hasTwoPlayers() {
        return players.size() >= MAX_PLAYERS;
    }

    public GameEngine start() throws GameEngineStarterException {
        if (!hasTwoPlayers()) {
            throw new GameEngineStarterException(String.format("Game engine need at least \"%s\" players", MAX_PLAYERS));
        }

        return new GameEngine(players, firstTurn, onGameOver);
    }
}
