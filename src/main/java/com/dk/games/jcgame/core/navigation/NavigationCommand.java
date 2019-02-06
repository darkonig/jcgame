package com.dk.games.jcgame.core.navigation;

import com.dk.games.jcgame.core.SceneParam;

public interface NavigationCommand {

    /**
     * Returns the new character position
     *
     * @param params
     * @return new character position
     */
    int execute(SceneParam params);

}
