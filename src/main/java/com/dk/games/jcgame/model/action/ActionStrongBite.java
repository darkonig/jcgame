package com.dk.games.jcgame.model.action;

public class ActionStrongBite extends Action {

    private static final int START_HIT_POINTS = 3;
    private static final long serialVersionUID = -6680184982080843786L;

    private int hitPoints = START_HIT_POINTS;

    public ActionStrongBite() {
        super("Strong Bite");
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
