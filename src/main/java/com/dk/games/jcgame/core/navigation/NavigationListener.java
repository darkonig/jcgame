package com.dk.games.jcgame.core.navigation;

import com.dk.games.jcgame.core.scene.Scene;
import com.dk.games.jcgame.core.scene.SceneFact;

public interface NavigationListener {

    void removeFacts(Scene scene);

    void saveViewFact(Scene scene, SceneFact fact);

}
