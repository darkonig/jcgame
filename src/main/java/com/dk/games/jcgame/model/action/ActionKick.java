package com.dk.games.jcgame.model.action;

public class ActionKick extends Action {

    private static final int START_HIT_POINTS = 3;
    private static final long serialVersionUID = -2872343372504889759L;

    private int hitPoints = START_HIT_POINTS;

    public ActionKick() {
        super("Kick");
    }

    @Override
    public int getHitPoints() {
        return hitPoints;
    }

    @Override
    public void setHitPoints(int points) {
        hitPoints = points;
    }

    @Override
    public int getStartHitPoints() {
        return START_HIT_POINTS;
    }
}
