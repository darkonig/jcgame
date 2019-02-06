package com.dk.games.jcgame.core;

import com.dk.games.jcgame.core.scene.Scene;
import com.dk.games.jcgame.model.Player;

public class SceneParam {
    private int paces;
    private Scene scene;
    private Player player;

    public int getPaces() {
        return paces;
    }

    public void setPaces(int paces) {
        this.paces = paces;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
