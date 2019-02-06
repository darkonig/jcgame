package com.dk.games.jcgame.model.action;

public class ActionStrongPunch extends Action {

    private static final int START_HIT_POINTS = 2;
    private static final long serialVersionUID = -6680184982080843786L;

    private int hitPoints = START_HIT_POINTS;

    public ActionStrongPunch() {
        super("Strong Punch");
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
