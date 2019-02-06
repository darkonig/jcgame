package com.dk.games.jcgame.service.impl;

import com.dk.games.jcgame.service.RepositoryService;
import com.dk.games.jcgame.service.exception.LoadException;
import com.dk.games.jcgame.service.exception.SaverException;

import java.io.*;
import java.util.Collection;
import java.util.Optional;

public class FileRepositoryService implements RepositoryService {

    @Override
    public <T> Optional<T> tryGetObject(String path) {
        try {
            return Optional.of(readObject(path));
        } catch (IOException | ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    @Override
    public <T> T getObject(String path) throws LoadException {
        try {
            return readObject(path);
        } catch (IOException | ClassNotFoundException e) {
            throw new LoadException(String.format("Error on loading %s. (%s)", path, e.getMessage()), e);
        }
    }

    @Override
    public <T> T save(String path, T obj) throws SaverException {
        try {
            saveObject(path, obj);
            return obj;
        } catch (IOException e) {
            throw new SaverException(String.format("Error on saving %s", path), e);
        }
    }

    @Override
    public void saveAll(String path, Collection<?> obj) throws SaverException {
        try {
            saveObject(path, obj);
        } catch (IOException e) {
            throw new SaverException(String.format("Error on saving %s", path), e);
        }
    }

    public <T> T readObject(String filename) throws IOException, ClassNotFoundException {
        try (FileInputStream fileInputStream = new FileInputStream(filename);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            return (T) objectInputStream.readObject();
        }
    }

    public void saveObject(String filename, Object obj) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filename);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
        }
    }
}
