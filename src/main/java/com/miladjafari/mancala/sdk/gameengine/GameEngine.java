package com.miladjafari.mancala.sdk.gameengine;

import com.miladjafari.mancala.sdk.Pit;
import com.miladjafari.mancala.sdk.Player;
import com.miladjafari.mancala.sdk.Playground;
import com.miladjafari.mancala.sdk.exception.GameEngineException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class GameEngine {

    private Map<Player, Playground> players;
    private Player nextTurn;
    private Boolean isGameOver = false;

    private Consumer<Map<Player, Playground>> onGameOver;

    GameEngine(Map<Player, Playground> players, Player firstTurn, Consumer<Map<Player, Playground>> onGameOver) {
        this.players = players;
        this.nextTurn = firstTurn;
        this.onGameOver = onGameOver;
    }

    public Map<Player, Playground> getPlayers() {
        return players;
    }

    public Player getNextTurn() {
        return nextTurn;
    }

    private Boolean isThePlayerTurn(Player player) {
        return (Optional.ofNullable(nextTurn).isPresent() && nextTurn.equals(player));
    }

    public void play(Player player, Integer selectedPitIndex) throws GameEngineException {
        Playground ownerPlayground = players.get(player);
        Playground opponentPlayground = players.get(findOpponentBy(player));

        if (isGameOver) {
            throw new GameEngineException("Game is over");
        }

        if (!isThePlayerTurn(player)) {
            throw new GameEngineException(String.format("It is not \"%s\" turn", player.getName()));
        }

        // reomcmeoe
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

    public Player findOpponentBy(Player currentPlayer) {
        for (Map.Entry<Player, Playground> entry : players.entrySet()) {
            if (!entry.getKey().equals(currentPlayer)) {
                return entry.getKey();
            }
        }

        return null;
    }

    private Player chooseNextTurnBy(Player currentPlayer, Boolean isLastStonePushedInBigPit) {
        return (isLastStonePushedInBigPit) ? currentPlayer : findOpponentBy(currentPlayer);
    }
}
