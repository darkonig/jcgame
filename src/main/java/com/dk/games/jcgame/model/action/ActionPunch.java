package com.dk.games.jcgame.model.action;

public class ActionPunch extends Action {

    private static final int START_HIT_POINTS = 1;
    private static final long serialVersionUID = 4038204970086043099L;

    private int hitPoints = START_HIT_POINTS;

    public ActionPunch() {
        super("Punch");
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
