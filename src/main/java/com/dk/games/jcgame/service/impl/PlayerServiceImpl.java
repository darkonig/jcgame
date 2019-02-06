package com.dk.games.jcgame.service.impl;

import com.dk.games.jcgame.model.GameConfig;
import com.dk.games.jcgame.model.SavePoint;
import com.dk.games.jcgame.service.PlayerService;
import com.dk.games.jcgame.service.RepositoryService;
import com.dk.games.jcgame.service.exception.SaverException;

import java.time.LocalDateTime;
import java.util.Optional;

public class PlayerServiceImpl implements PlayerService {
    private final GameConfig config;
    private final RepositoryService repository;

    public PlayerServiceImpl(GameConfig config, RepositoryService repository) {
        this.config = config;
        this.repository = repository;
    }

    @Override
    public SavePoint save(SavePoint savePoint) throws SaverException {
        savePoint.setDate(LocalDateTime.now());

        repository.save(config.getSavedData(), savePoint);
        return savePoint;
    }

    @Override
    public Optional<SavePoint> getSavePoint() {
        return repository.tryGetObject(config.getSavedData());
    }

}
