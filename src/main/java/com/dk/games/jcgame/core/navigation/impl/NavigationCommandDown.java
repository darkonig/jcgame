package com.dk.games.jcgame.core.navigation.impl;

import com.dk.games.jcgame.core.SceneParam;
import com.dk.games.jcgame.core.navigation.NavigationCommand;

public class NavigationCommandDown implements NavigationCommand {

    @Override
    public int execute(SceneParam params) {
        return params.getScene().getPosition() + params.getScene().getRowLength() * params.getPaces();
    }

}
