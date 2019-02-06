package com.dk.games.jcgame.service;

import com.dk.games.jcgame.core.scene.Scene;
import com.dk.games.jcgame.service.exception.LoadException;

public interface SceneService {

    Scene loadScene(String previousScene, String sceneName) throws LoadException;

    Scene loadScene(String previousScene, String sceneName, String filePath) throws LoadException;
}
