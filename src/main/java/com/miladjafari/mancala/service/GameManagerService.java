package com.miladjafari.mancala.service;

import com.miladjafari.mancala.repository.GameEngineRepository;
import com.miladjafari.mancala.repository.GameEngineStarterRepository;
import com.miladjafari.mancala.sdk.Player;
import com.miladjafari.mancala.sdk.exception.GameEngineException;
import com.miladjafari.mancala.sdk.exception.GameEngineStarterException;
import com.miladjafari.mancala.sdk.exception.GameManagerException;
import com.miladjafari.mancala.sdk.gameengine.GameEngine;
import com.miladjafari.mancala.sdk.gameengine.GameEngineStarter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;
import java.util.logging.Logger;

@ApplicationScoped
public class GameManagerService {

    private static final Logger logger = Logger.getLogger(GameManagerService.class.getName());

    @Inject
    GameEngineStarterRepository gameEngineStarterRepository;

    @Inject
    GameEngineRepository gameEngineRepository;

    public String createNewRoom() {
        String gameId = UUID.randomUUID().toString();
        GameEngineStarter gameEngineStarter = new GameEngineStarter();
        gameEngineStarterRepository.add(gameId, gameEngineStarter);

        logger.info("New game has been created but not started");
        return gameId;
    }

    public void addPlayer(String gameId, String playerName) {
        GameEngineStarter gameEngineStarter = gameEngineStarterRepository.findById(gameId)
                                                                         .orElseThrow(() -> new GameManagerException(
                                                                                 "GameEngineStarter could not find"));
        try {
            gameEngineStarter.addPlayer(new Player(playerName));
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
            //fireStartEvent
        }
    }

    public void play(String gameId, String player, Integer pitIndex) {
        GameEngine gameEngineStarter = gameEngineRepository.findById(gameId)
                                                           .orElseThrow(() -> new GameManagerException(
                                                                   String.format("Could not find game %s", gameId)));
        try {
            gameEngineStarter.play(new Player(player), pitIndex);
        } catch (GameEngineException exception) {
            throw new GameManagerException(exception);
        }
    }
}