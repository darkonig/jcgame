package com.dk.games.jcgame.model.action;

public class ActionCrazyGnaw extends MagicAction {

    private static final int START_HIT_POINTS = 3;
    private static final long serialVersionUID = -7439008093542087505L;

    private int hitPoints = START_HIT_POINTS;
    private final int staminaPoints = 3;

    public ActionCrazyGnaw() {
        super("Crazy Gnaw");
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
