package com.miladjafari.mancala.sdk.gameengine;

import com.miladjafari.mancala.sdk.Pit;
import com.miladjafari.mancala.sdk.Playground;
import com.miladjafari.mancala.sdk.exception.GameEngineException;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class GameEngine {
    private final Consumer<Map<String, Playground>> onGameOver;
    private final Map<String, Playground> players;

    private Boolean isGameOver = false;
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
