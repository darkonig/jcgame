package com.dk.games.jcgame.service.impl;

import com.dk.games.jcgame.model.IBattleChar;
import com.dk.games.jcgame.model.GameConfig;
import com.dk.games.jcgame.service.CharService;
import com.dk.games.jcgame.service.RepositoryService;
import com.dk.games.jcgame.service.exception.LoadException;
import com.dk.games.jcgame.service.exception.SaverException;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CharServiceImpl implements CharService {
    private final GameConfig config;
    private final RepositoryService repository;

    private static List<IBattleChar> charsCache;

    public CharServiceImpl(GameConfig config, RepositoryService repository) {
        this.config = config;
        this.repository = repository;
    }

    @Override
    public void saveAll(List<IBattleChar> chars) throws SaverException {
        if (charsCache == null) {
            charsCache = chars;
        }

        repository.saveAll(config.getCharacters(), chars);
    }

    @Override
    public List<IBattleChar> getChars() throws LoadException {
        if (charsCache == null) {
            charsCache = repository.getObject(config.getCharacters());
        }

        return charsCache;
    }

    @Override
    public IBattleChar createChar(String name) throws LoadException {
        List<IBattleChar> chars = this.getChars();
        if (!chars.isEmpty()) {
            Optional<IBattleChar> first = chars.stream().filter(e -> e.getName().equals(name)).findFirst();
            if (first.isPresent()) {
                return (IBattleChar) first.get().copy();
            }
        }

        return null;
    }

    @Override
    public IBattleChar getCharWithLevel(int level, boolean hero) throws LoadException {
        List<IBattleChar> chars = this.getChars();
        if (!chars.isEmpty()) {
            List<IBattleChar> any = chars.stream().filter(e -> e.getLevel() == level
                    && hero == e.isHero()).collect(Collectors.toList());
            if (any.size() > 0) {
                return (IBattleChar) any.get(new Random().nextInt(any.size())).copy();
            }
        }

        return null;
    }

    public void resetCache() {
        charsCache = null;
    }
}
