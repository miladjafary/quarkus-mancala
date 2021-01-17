package com.miladjafari.mancala.service;

import com.miladjafari.mancala.repository.GameEngineStarterRepository;
import com.miladjafari.mancala.sdk.Player;
import com.miladjafari.mancala.sdk.exception.GameRoomException;
import com.miladjafari.mancala.sdk.gameengine.GameEngineStarter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;
import java.util.logging.Logger;

@ApplicationScoped
public class GameRoomService {

    private static final Logger logger = Logger.getLogger(GameRoomService.class.getName());

    @Inject
    GameEngineStarterRepository gameEngineStarterRepository;

    public String createNewRoom() {
        String gameId = UUID.randomUUID().toString();
        GameEngineStarter gameEngineStarter = new GameEngineStarter();
        gameEngineStarterRepository.add(gameId, gameEngineStarter);

        logger.info("New game has been created but not started");
        return gameId;
    }

    public void addPlayer(String gameId, String playerName) {
        GameEngineStarter gameEngineStarter = gameEngineStarterRepository.findById(gameId)
                                                                         .orElseThrow(() -> new GameRoomException(
                                                                                 "Game Engine can be found "));
        try {
            gameEngineStarter.addPlayer(new Player(playerName));
        }catch (IllegalArgumentException exception) {
            throw new GameRoomException(exception.getMessage());
        }
    }
}
