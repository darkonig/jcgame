package com.dk.games.jcgame.core.navigation.impl;

import com.dk.games.jcgame.constant.GameConstants;
import com.dk.games.jcgame.core.navigation.NavigationListener;
import com.dk.games.jcgame.core.navigation.NavigationProcessor;
import com.dk.games.jcgame.core.scene.Scene;
import com.dk.games.jcgame.core.scene.SceneFact;
import com.dk.games.jcgame.core.scene.SceneFactProcessor;
import com.dk.games.jcgame.model.Player;
import com.dk.games.jcgame.service.SceneService;
import com.dk.games.jcgame.service.exception.LoadException;

public class NavigationProcessorSceneFact implements NavigationProcessor {
    private final NavigationProcessor parent;
    private final SceneFact sceneFact;
    private final NavigationListener listener;
    private SceneFactProcessor factProcessor;
    private Scene scene;

    public NavigationProcessorSceneFact(NavigationProcessor parent, SceneService sceneService
            , Player player, SceneFact sceneFact, NavigationListener listener) throws LoadException {
        this.parent = parent;
        this.sceneFact = sceneFact;
        this.scene = parent.getScene().copy();
        this.listener = listener;

        Scene infoScene;
        if (sceneFact.isFullInfo()) {
            infoScene = sceneService.loadScene(scene.getName(), GameConstants.SCENE_FACT_FULL_TEXT);
        } else {
            infoScene = sceneService.loadScene(scene.getName(), GameConstants.SCENE_FACT_TEXT);
        }

        // render a fact
        factProcessor = scene.getSceneFactProcessor(player, infoScene, sceneFact);

        scene.setScene(factProcessor.getSceneNext());
    }

    @Override
    public NavigationProcessor next(String input) throws LoadException {
        boolean ok = ">".equals(input);

        if (ok) {
            if (factProcessor.isFinished()) {
                // save current fact showed
                if (listener != null) {
                    listener.saveViewFact(parent.getScene(), sceneFact);
                }
                return parent.next("");
            }

            scene.setScene(factProcessor.getSceneNext());
        }

        return this;
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean isWaitUserInput() {
        return true;
    }

    @Override
    public long freezeSceneMilis() {
        return 0;
    }
}
