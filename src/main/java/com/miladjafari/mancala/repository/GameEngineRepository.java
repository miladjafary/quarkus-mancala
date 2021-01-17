package com.miladjafari.mancala.repository;

import com.miladjafari.mancala.sdk.gameengine.GameEngine;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class GameEngineRepository {
    private final Map<String, GameEngine> engines = new HashMap<>();

    public void add(String id, GameEngine gameEngine) {
        engines.put(id, gameEngine);
    }

    public Optional<GameEngine> findById(String id) {
        return Optional.ofNullable(engines.get(id));
    }
}
