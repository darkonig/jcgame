package com.dk.games.jcgame.service;

import com.dk.games.jcgame.model.SavePoint;
import com.dk.games.jcgame.service.exception.SaverException;

import java.util.Optional;

public interface PlayerService {

    SavePoint save(SavePoint savePoint) throws SaverException;

    Optional<SavePoint> getSavePoint();

}
