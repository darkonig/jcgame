package com.dk.games.jcgame.core.navigation.impl;

import com.dk.games.jcgame.constant.GameConstants;
import com.dk.games.jcgame.core.SceneParam;
import com.dk.games.jcgame.core.navigation.NavigationCommand;
import com.dk.games.jcgame.core.scene.DialogScene;
import com.dk.games.jcgame.service.RenderService;
import com.dk.games.jcgame.service.SceneService;
import com.dk.games.jcgame.service.exception.LoadException;

public class NavigationCommandStatus implements NavigationCommand {

    private final SceneService sceneService;
    private final RenderService renderService;

    public NavigationCommandStatus(SceneService sceneService, RenderService renderService) {
        this.sceneService = sceneService;
        this.renderService = renderService;
    }

    @Override
    public int execute(SceneParam params) {
        try {
            DialogScene status = new DialogScene(sceneService.loadScene(null, GameConstants.SCENE_PLAYER_STATUS), params.getPlayer());
            String input;
            do {
                renderService.render(status.getScene());

                input = renderService.getUserInput();
            } while (!status.isFinished(input));
        } catch (LoadException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
