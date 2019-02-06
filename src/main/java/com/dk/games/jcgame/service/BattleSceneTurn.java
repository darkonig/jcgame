package com.dk.games.jcgame.service;

import com.dk.games.jcgame.core.scene.BattleScene;
import com.dk.games.jcgame.core.scene.Scene;

public class BattleSceneTurn {

    private final boolean playerTurn;
    private final BattleScene battleScene;
    private final boolean finished;

    public BattleSceneTurn(boolean playerTurn, BattleScene battleScene, boolean finished) {
        this.playerTurn = playerTurn;
        this.battleScene = battleScene;
        this.finished = finished;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public BattleScene getBattleScene() {
        return battleScene;
    }

    public Scene getScene() {
        return battleScene.getScene();
    }

    public boolean isFinished() {
        return finished;
    }
}
