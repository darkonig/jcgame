package com.dk.games.jcgame.core.scene;

import com.dk.games.jcgame.model.Player;

public class DialogScene {

    private final Scene scene;

    private final Player player;

    public DialogScene(Scene scene, Player player) {
        this.scene = scene;
        this.player = player;
        processTexts();
    }

    public Scene getScene() {
        return scene;
    }

    public void update() {
        processTexts();
    }

    private void processTexts() {
        BaseSceneActions s = new BaseSceneActions();
        s.setShowActionHitPoints(true);
        s.addProcessor(player, false);
        scene.setScene(s.processTexts(scene));
    }

    public boolean isFinished(String input) {
        return "<".equals(input);
    }
}
