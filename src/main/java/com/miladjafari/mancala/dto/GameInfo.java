package com.miladjafari.mancala.dto;

import java.util.HashMap;
import java.util.Map;

public class GameInfo {
    private String gameId;
    private String player;
    private String opponent;
    private String winner = "";

    private Map<String, Integer> boardStatus = new HashMap<>();
    private String nextTurn;
    private boolean isGameOver = false;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public Map<String, Integer> getBoardStatus() {
        return boardStatus;
    }

    public void setBoardStatus(Map<String, Integer> boardStatus) {
        this.boardStatus = boardStatus;
    }

    public String getNextTurn() {
        return nextTurn;
    }

    public void setNextTurn(String nextTurn) {
        this.nextTurn = nextTurn;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

}
