package com.dk.games.jcgame.core.scene;

import com.dk.games.jcgame.model.Enemy;

public class SceneMoveAction {

    private String nextScene;
    private SceneFact sceneFact;
    private Enemy enemy;

    private static final SceneMoveAction empty = new SceneMoveAction("", null, null);

    public static SceneMoveAction empty() {
        return empty;
    }

    public static SceneMoveAction create(String nextScene, Enemy enemy) {
        return new SceneMoveAction(nextScene, enemy, null);
    }

    public static SceneMoveAction create(SceneFact fact) {
        return new SceneMoveAction("", null, fact);
    }

    private SceneMoveAction(String nextScene, Enemy enemy, SceneFact sceneFact) {
        this.nextScene = nextScene;
        this.enemy = enemy;
        this.sceneFact = sceneFact;
    }

    public String getNextScene() {
        return nextScene;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public SceneFact getSceneFact() {
        return sceneFact;
    }

    public boolean hasNextScene() {
        return !nextScene.isEmpty();
    }

    public boolean hasSceneFact() {
        return sceneFact != null;
    }
}
