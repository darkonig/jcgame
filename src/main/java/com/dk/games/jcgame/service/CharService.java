package com.dk.games.jcgame.service;

import com.dk.games.jcgame.model.BattleChar;
import com.dk.games.jcgame.model.IBattleChar;
import com.dk.games.jcgame.service.exception.LoadException;
import com.dk.games.jcgame.service.exception.SaverException;

import java.util.List;

public interface CharService {

    void saveAll(List<IBattleChar> chars) throws SaverException;

    List<IBattleChar> getChars() throws LoadException;

    IBattleChar createChar(String name) throws LoadException;

    IBattleChar getCharWithLevel(int level, boolean hero) throws LoadException;

}
