package com.dk.games.jcgame.model.action;

public class ActionSuperScratch extends MagicAction {

    private static final int START_HIT_POINTS = 5;
    private static final long serialVersionUID = -9002470189535011345L;

    private int hitPoints = START_HIT_POINTS;
    private final int staminaPoints = 3;

    public ActionSuperScratch() {
        super("Super Scratch");
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
