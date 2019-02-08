package com.dk.games.jcgame.model.action;

public class ActionCrazyHowl extends MagicAction {

    private static final int START_HIT_POINTS = 5;
    private static final long serialVersionUID = 5405650000410257049L;

    private int hitPoints = START_HIT_POINTS;
    private final int staminaPoints = 3;

    public ActionCrazyHowl() {
        super("Crazy Howl");
    }

    @Override
    public int getStaminaPoints() {
        return staminaPoints;
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
