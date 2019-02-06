package com.dk.games.jcgame.model;

import com.dk.games.jcgame.core.scene.Scene;
import com.dk.games.jcgame.core.scene.SceneFact;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class SavePoint implements Serializable {

    private static final long serialVersionUID = 6749619488980028482L;

    private LocalDateTime date;
    private Player player;
    private Scene currentScene;
    private List<SceneFact> viewedFacts;

    public SavePoint() {
        viewedFacts = new ArrayList<>();
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public List<SceneFact> getViewedFacts() {
        return viewedFacts;
    }

    public void addViewedFacts(SceneFact viewedFact) {
        this.viewedFacts.add(viewedFact);
    }
}
