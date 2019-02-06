package com.dk.games.jcgame.service.impl;

import com.dk.games.jcgame.model.GameConfig;
import com.dk.games.jcgame.core.scene.Scene;
import com.dk.games.jcgame.service.SceneService;
import com.dk.games.jcgame.service.exception.LoadException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SceneServiceImpl implements SceneService {

    private final GameConfig config;

    public SceneServiceImpl(GameConfig config) {
        this.config = config;
    }

    @Override
    public Scene loadScene(String previousScene, String sceneName) throws LoadException {
        return loadScene(previousScene, sceneName, sceneName + ".scene");
    }

    @Override
    public Scene loadScene(String previousScene, String sceneName, String filePath) throws LoadException {
        Scene scene = new Scene(sceneName, previousScene);

        try {
            String resource = getFile(filePath);

            if (resource == null) {
                throw new LoadException(String.format("Error on loading scene %s, file not found.", filePath));
            }

            scene.setBaseScene( new String(readFile(resource)) );
        } catch (IOException e) {
            throw new LoadException(String.format("Error on loading scene %s. (%s)", sceneName, e.getMessage()), e);
        }

        return scene;
    }

    public byte[] readFile(String resource) throws IOException {
        return Files.readAllBytes(Paths.get( resource ));
    }

    public String getFile(String fileName) {
        return "scenes/" + fileName;
    }
}
