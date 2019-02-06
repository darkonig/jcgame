package com.dk.games.jcgame.core.rule;

import java.io.Serializable;

public class LevelUpRuleImpl implements LevelUpRule, Serializable {

    private static final long serialVersionUID = 6091918454144140539L;

    @Override
    public int getHitPoints(int startHitPoint, int level) {
        return Math.round(startHitPoint * (float) Math.pow(1.2, level));
    }

}
