package com.dk.games.jcgame.model.action;

public abstract class MagicAction extends Action {
    private static final long serialVersionUID = 1L;

    public MagicAction(String name) {
        super(name);
    }

    public abstract int getStaminaPoints();

    @Override
    public String toString() {
        return getName() + " (St: " + getStaminaPoints() + ")";
    }
}
