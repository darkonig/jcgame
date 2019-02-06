package com.dk.games.jcgame.core.navigation.impl;

import com.dk.games.jcgame.core.SceneParam;
import com.dk.games.jcgame.core.navigation.NavigationCommand;

public class NavigationCommandLeft implements NavigationCommand {

    @Override
    public int execute(SceneParam params) {
        return params.getScene().getPosition() - params.getPaces();
    }

}
