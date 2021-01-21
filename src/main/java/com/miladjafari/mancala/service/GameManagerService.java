package com.miladjafari.mancala.service;

import com.miladjafari.mancala.dto.GameEvent;
import com.miladjafari.mancala.dto.GameInfo;
import com.miladjafari.mancala.repository.GameEngineRepository;
import com.miladjafari.mancala.repository.GameEngineStarterRepository;
import com.miladjafari.mancala.sdk.exception.GameEngineException;
import com.miladjafari.mancala.sdk.exception.GameEngineStarterException;
import com.miladjafari.mancala.sdk.exception.GameManagerException;
import com.miladjafari.mancala.sdk.gameengine.GameEngine;
import com.miladjafari.mancala.sdk.gameengine.GameEngineStarter;
import com.miladjafari.mancala.websocket.GameNotifier;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Manage the whole game to enable running multiple games simultaneously
 */
@ApplicationScoped
public class GameManagerService {

    private static final Logger logger = Logger.getLogger(GameManagerService.class.getName());

    @Inject
    GameEngineStarterRepository gameEngineStarterRepository;

    @Inject
    GameEngineRepository gameEngineRepository;

    @Inject
    GameNotifier gameNotifier;

    public String createNewRoom() {
        String gameId = UUID.randomUUID().toString();
        GameEngineStarter gameEngineStarter = new GameEngineStarter()
                .gameId(gameId)
                .onGameOver(this::fireFinishEvent)
                ;
        gameEngineStarterRepository.add(gameId, gameEngineStarter);

        logger.info("New game has been created but not started");
        return gameId;
    }

    public void addPlayer(String gameId, String playerName) {
        GameEngineStarter gameEngineStarter = gameEngineStarterRepository.findById(gameId)
                                                                         .orElseThrow(() -> new GameManagerException(
                                                                                 "GameEngineStarter could not find"));
        try {
            gameEngineStarter.addPlayer(playerName);
            startGameIfTwoPlayersExists(gameId, gameEngineStarter);
        } catch (IllegalArgumentException | GameEngineStarterException exception) {
            throw new GameManagerException(exception);
        }
    }

    private void startGameIfTwoPlayersExists(String gameId, GameEngineStarter gameEngineStarter)
            throws GameEngineStarterException {
        if (gameEngineStarter.hasTwoPlayers()) {
            GameEngine gameEngine = gameEngineStarter.start();
            gameEngineRepository.add(gameId, gameEngine);
            logger.info(String.format("Game Engine has been started for game id [%s]", gameId));

            fireGameStartEvent(gameId);
        }
    }

    public Map<String, Integer> play(String gameId, String player, Integer pitIndex) {
        String errorMessage = String.format("Game with id [%s] could not find or has not started yet.", gameId);
        GameEngine gameEngine = gameEngineRepository.findById(gameId)
                                                    .orElseThrow(() -> new GameManagerException(errorMessage));
        try {
            gameEngine.play(player, pitIndex);
            if (!gameEngine.isGameOver()) {
                fireBoardChangeEvent(gameId);
            }

            return gameEngine.getBoardStatusOf(player);
        } catch (IllegalArgumentException | GameEngineException exception) {
            throw new GameManagerException(exception);
        }
    }

    public GameInfo getGameInfo(String gameId, String player) {
        String errorMessage = String.format("Game with id [%s] could not find or has not started yet.", gameId);
        GameEngine gameEngine = gameEngineRepository.findById(gameId)
                                                    .orElseThrow(() -> new GameManagerException(errorMessage));

        try {
            Map<String, Integer> boardStatus = gameEngine.getBoardStatusOf(player);

            GameInfo gameInfo = new GameInfo();
            gameInfo.setGameId(gameId);
            gameInfo.setPlayer(player);
            gameInfo.setOpponent(gameEngine.findOpponentBy(player));

            gameInfo.setBoardStatus(boardStatus);
            gameInfo.setNextTurn(gameEngine.getNextTurn());
            gameInfo.setGameOver(gameEngine.isGameOver());

            return gameInfo;
        } catch (GameEngineException exception) {
            throw new GameManagerException(exception);
        }
    }

    private void fireGameStartEvent(String gameId) {
        GameEvent event = GameEvent.builder()
                                   .gameId(gameId)
                                   .gameStarted()
                                   .build();
        gameNotifier.onStart(event);
    }

    private void fireBoardChangeEvent(String gameId) {
        GameEvent event = GameEvent.builder()
                                   .gameId(gameId)
                                   .gameBoardChange()
                                   .build();
        gameNotifier.onBoardChange(event);
    }

    private void fireFinishEvent(GameInfo gameInfo) {
        GameEvent event = GameEvent.builder()
                                   .gameId(gameInfo.getGameId())
                                   .gameOver()
                                   .build();
        gameNotifier.onFinish(event);
    }
}
