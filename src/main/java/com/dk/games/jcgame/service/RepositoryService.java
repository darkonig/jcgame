package com.dk.games.jcgame.service;

import com.dk.games.jcgame.service.exception.LoadException;
import com.dk.games.jcgame.service.exception.SaverException;

import java.util.Collection;
import java.util.Optional;

public interface RepositoryService {

    <T> Optional<T> tryGetObject(String path);

    <T> T getObject(String path) throws LoadException;

    <T> T save(String path, T obj) throws SaverException;

    void saveAll(String path, Collection<?> obj) throws SaverException;

}
