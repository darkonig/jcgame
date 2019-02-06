package com.dk.games.jcgame.core.navigation.impl;

import com.dk.games.jcgame.core.SceneParam;
import com.dk.games.jcgame.core.navigation.NavigationCommand;
import com.dk.games.jcgame.model.SavePoint;
import com.dk.games.jcgame.service.PlayerService;
import com.dk.games.jcgame.service.exception.SaverException;

import java.util.Optional;

public class NavigationCommandExit implements NavigationCommand {

    private PlayerService playerService;

    public NavigationCommandExit(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public int execute(SceneParam params) {
        Optional<SavePoint> opSavePoint = playerService.getSavePoint();

        SavePoint savePoint = opSavePoint.orElse(new SavePoint());
        savePoint.setPlayer(params.getPlayer());
        savePoint.setCurrentScene(params.getScene());

        try {
            playerService.save(savePoint);
        } catch (SaverException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
