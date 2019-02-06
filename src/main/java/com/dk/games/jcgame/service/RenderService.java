package com.dk.games.jcgame.service;

import com.dk.games.jcgame.core.scene.Scene;
import com.dk.games.jcgame.service.exception.InvalidSceneException;

public interface RenderService {

    void init();

    void stop();

    void render(Scene scene) throws InvalidSceneException;

    String getUserInput();

    void freezeScene(long milis);

}
