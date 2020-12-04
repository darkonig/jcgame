package com.dk.games.jcgame.model.action;

//IMPROV too much code repetition
public class ActionBite extends Action {

    private static final int START_HIT_POINTS = 1;
    private static final long serialVersionUID = -4565318637800847416L;

    private int hitPoints = START_HIT_POINTS;

    public ActionBite() {
        super("Bite");
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
