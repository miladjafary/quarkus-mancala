package com.miladjafari.mancala.sdk.gameengine;

import com.miladjafari.mancala.sdk.Pit;
import com.miladjafari.mancala.sdk.Playground;
import com.miladjafari.mancala.sdk.exception.GameEngineException;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * GameEngine is responsible for handling a Mancala game's rules between two player.
 */
public class GameEngine {
    /**
     * A listener that is called when game is over.
     */
    private final Consumer<Map<String, Playground>> onGameOver;

    /**
     * Map of players name with their {@link Playground}
     */
    private final Map<String, Playground> players;

    /**
     * Keeps status of running game, it will be true if game is over
     */
    private Boolean isGameOver = false;

    /**
     * Keeps next player turn.
     */
    private String nextTurn;

    GameEngine(
            Map<String, Playground> players,
            String firstTurn,
            Consumer<Map<String, Playground>> onGameOver) {
        this.players = players;
        this.nextTurn = firstTurn;
        this.onGameOver = onGameOver;
    }

    public Map<String, Playground> getPlayers() {
        return players;
    }

    public String getNextTurn() {
        return nextTurn;
    }

    private Boolean isThePlayerTurn(String player) {
        return (Optional.ofNullable(nextTurn).isPresent() && nextTurn.equals(player));
    }

    /**
     * Return board status from player point of view in a clockwise in which pit #7 is the player big pit
     * and pit #14 is the opponent big pit. pits number 1 to 6 is the player's small pits and pit 8 to 13 is
     * the inverse of the opponent's small pit.
     *
     * <p>Example:</p>
     * <pre>
     *  Player Pit | Opponent Pit
     *  -----------|---------------
     *       13    |     01
     *       12    |     02
     *       11    |     03
     *       10    |     04
     *       09    |     05
     *       08    |     06
     * </pre>
     *
     * @param player name of player
     * @return map of pit index with number of stones on each of them from player point of view.
     */
    public Map<String, Integer> getBoardStatusOf(String player) {
        Map<String, Integer> boardStatus = new HashMap<>();
        Playground ownerPlayground = players.get(player);
        Playground opponentPlayground = players.get(findOpponentBy(player));

        sortAndAddPlaygroundsPitToBoardStatus(ownerPlayground,
                                              boardStatus,
                                              Comparator.comparingInt(Pit::getIndex));

        sortAndAddPlaygroundsPitToBoardStatus(opponentPlayground,
                                              boardStatus,
                                              Comparator.comparingInt(Pit::getIndex).reversed());

        return boardStatus;
    }

    private void sortAndAddPlaygroundsPitToBoardStatus(
            Playground playground,
            Map<String, Integer> boardStatus,
            Comparator<Pit> comparator
    ) {
        //add small pits
        playground.getSmallPits()
                  .stream()
                  .sorted(comparator)
                  .forEach(pit -> {
                      int pitIndex = boardStatus.size() + 1;
                      boardStatus.put(Integer.toString(pitIndex), pit.getCountOfStones());
                  });

        //add big pit
        boardStatus.put(Integer.toString(boardStatus.size() + 1), playground.getBigPit().getCountOfStones());
    }

    /**
     * Enable a player to play the stones of a pit
     * @param player player name
     * @param selectedPitIndex selected pit to move its stones to other pits
     *
     * @throws GameEngineException
     *  <ul>
     *      <li>in case of game is over</li>
     *      <li>in case of not player's turn</li>
     *  </ul>
     *
     */
    public void play(String player, Integer selectedPitIndex) throws GameEngineException {
        Playground ownerPlayground = players.get(player);
        Playground opponentPlayground = players.get(findOpponentBy(player));

        if (isGameOver) {
            throw new GameEngineException("Game is over");
        }

        if (!isThePlayerTurn(player)) {
            throw new GameEngineException(String.format("It is not \"%s\" turn", player));
        }

        Pit pit = ownerPlayground.getPit(selectedPitIndex);
        validatingSelectedPit(pit);

        MancalaBoard mancalaBoard = new MancalaBoard()
                .ownerPlayground(ownerPlayground)
                .opponentPlayground(opponentPlayground)
                .selectedPit(selectedPitIndex)
                .play();

        nextTurn = chooseNextTurnBy(player, mancalaBoard.isLastStonePushedInBigPit());

        if (isEndOfTheGame()) {
            isGameOver = true;
            onGameOver.accept(players);
        }
    }

    private Boolean isEndOfTheGame() {
        for (Playground playground : players.values()) {
            if (playground.areAllSmallPitsEmpty()) {
                return true;
            }
        }

        return false;
    }

    private void validatingSelectedPit(Pit pit) throws GameEngineException {
        if (pit.isBigPit()) {
            throw new GameEngineException("Moving from big pit is not allowed");
        }

        if (pit.isEmpty()) {
            throw new GameEngineException("Moving from empty pit is not allowed");
        }
    }

    /**
     * Finds the player's opponent
     * @param currentPlayer player's name
     * @return opponent player
     */
    public String findOpponentBy(String currentPlayer) {
        for (Map.Entry<String, Playground> entry : players.entrySet()) {
            if (!entry.getKey().equals(currentPlayer)) {
                return entry.getKey();
            }
        }

        return null;
    }

    private String chooseNextTurnBy(String currentPlayer, Boolean isLastStonePushedInBigPit) {
        return (isLastStonePushedInBigPit) ? currentPlayer : findOpponentBy(currentPlayer);
    }
}
