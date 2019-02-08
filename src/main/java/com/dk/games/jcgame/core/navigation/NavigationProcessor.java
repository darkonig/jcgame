package com.dk.games.jcgame.core.navigation;

import com.dk.games.jcgame.core.scene.Scene;
import com.dk.games.jcgame.service.exception.LoadException;

public interface NavigationProcessor {

    NavigationProcessor next(String input) throws LoadException;

    Scene getScene();

    boolean isFinished();

    boolean isWaitUserInput();

    long freezeSceneMilis();

}
