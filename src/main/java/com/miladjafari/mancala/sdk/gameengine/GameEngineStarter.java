package com.miladjafari.mancala.sdk.gameengine;

import com.miladjafari.mancala.dto.GameInfo;
import com.miladjafari.mancala.sdk.Playground;
import com.miladjafari.mancala.sdk.exception.GameEngineStarterException;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * This class is responsible to prepare a {@link GameEngine}.
 * In another words, this is a builder for {@link GameEngine}
 */
public class GameEngineStarter {
    private static final Integer MAX_PLAYERS = 2;
    private static final Integer NUMBER_OF_PITS = 6;
    private static final Integer NUMBER_OF_STONE_IN_EACH_PIT = 6;

    /**
     * Keeps map of onboarded player's and their playground
     */
    private final Map<String, Playground> players = new TreeMap<>();

    /**
     * Unique id of the game.
     */
    private String gameId = UUID.randomUUID().toString();

    /**
     * Keep the first player trun
     */
    private String firstTurn;

    /**
     * Default listener which is called after the game is over
     */
    private Consumer<GameInfo> onGameOver = gameInfo -> {};

    public GameEngineStarter gameId(String gameId) {
        this.gameId = gameId;
        return this;
    }

    /**
     * Add new player. It will controller duplicate player and prevents adding more than
     * {@link GameEngineStarter#MAX_PLAYERS}
     *
     * @param player player name
     * @return current instance of {@link GameEngineStarter}
     */
    public GameEngineStarter addPlayer(String player) {
        if (hasTwoPlayers()) {
            throw new IllegalArgumentException(String.format("Maximum number of players is %s.", MAX_PLAYERS));
        }

        if (isExist(player)) {
            throw new IllegalArgumentException(String.format("Player \"%s\" is exist.", player));
        }

        Playground playground = createPlayground();
        players.put(player, playground);
        assignFirstTurnToPlayerIfFirstTurnIsNull(player);

        return this;
    }

    public GameEngineStarter onGameOver(Consumer<GameInfo> onGameOver) {
        this.onGameOver = onGameOver;
        return this;
    }

    private void assignFirstTurnToPlayerIfFirstTurnIsNull(String player) {
        firstTurn = Optional.ofNullable(firstTurn).orElse(player);
    }

    private Playground createPlayground() {
        return Playground.builder()
                         .numberOfPits(NUMBER_OF_PITS)
                         .numberOfStonesInEachPit(NUMBER_OF_STONE_IN_EACH_PIT)
                         .build();
    }

    private Boolean isExist(String player) {
        return players.containsKey(player);
    }

    public Boolean hasTwoPlayers() {
        return players.size() >= MAX_PLAYERS;
    }

    /**
     * Create an instance of {@link GameEngine}
     *
     * @return an instance of {@link GameEngine}
     * @throws GameEngineStarterException
     * <ul>
     *     <li>Game Id is not been set</li>
     *     <li>In case two player do not present.</li>
     * </ul>
     *
     */
    public GameEngine start() throws GameEngineStarterException {
        if (gameId == null || gameId.isEmpty()) {
            throw new GameEngineStarterException("Game id is required");
        }

        if (!hasTwoPlayers()) {
            throw new GameEngineStarterException(
                    String.format("Game engine need at least \"%s\" players", MAX_PLAYERS));
        }

        return new GameEngine(gameId, players, firstTurn, onGameOver);
    }
}
