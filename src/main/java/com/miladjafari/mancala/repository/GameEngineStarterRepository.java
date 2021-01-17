package com.miladjafari.mancala.repository;

import com.miladjafari.mancala.sdk.gameengine.GameEngineStarter;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class GameEngineStarterRepository {
    private final Map<String, GameEngineStarter> engineStarters = new HashMap<>();

    public void add(String id, GameEngineStarter gameEngineStarter) {
        engineStarters.put(id, gameEngineStarter);
    }

    public Optional<GameEngineStarter> findById(String id) {
        return Optional.ofNullable(engineStarters.get(id));
    }
}
