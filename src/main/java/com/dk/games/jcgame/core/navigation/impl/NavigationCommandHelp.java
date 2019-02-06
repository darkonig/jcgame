package com.dk.games.jcgame.core.navigation.impl;

import com.dk.games.jcgame.constant.GameConstants;
import com.dk.games.jcgame.core.SceneParam;
import com.dk.games.jcgame.core.navigation.NavigationCommand;
import com.dk.games.jcgame.core.scene.DialogScene;
import com.dk.games.jcgame.service.RenderService;
import com.dk.games.jcgame.service.SceneService;
import com.dk.games.jcgame.service.exception.LoadException;

public class NavigationCommandHelp implements NavigationCommand {

    private final SceneService sceneService;
    private final RenderService renderService;

    public NavigationCommandHelp(SceneService sceneService, RenderService renderService) {
        this.sceneService = sceneService;
        this.renderService = renderService;
    }

    @Override
    public int execute(SceneParam params) {
        try {
            DialogScene status = new DialogScene(sceneService.loadScene(null, GameConstants.SCENE_HELP), params.getPlayer());
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
