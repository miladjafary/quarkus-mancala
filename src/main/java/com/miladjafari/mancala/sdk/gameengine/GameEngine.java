package com.miladjafari.mancala.sdk.gameengine;

import com.miladjafari.mancala.sdk.Pit;
import com.miladjafari.mancala.sdk.Playground;
import com.miladjafari.mancala.sdk.exception.GameEngineException;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class GameEngine {

    private final Map<String, Playground> players;
    private String nextTurn;
    private Boolean isGameOver = false;

    private final Consumer<Map<String, Playground>> onGameOver;

    GameEngine(Map<String, Playground> players, String firstTurn, Consumer<Map<String, Playground>> onGameOver) {
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
