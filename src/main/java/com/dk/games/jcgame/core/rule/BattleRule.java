package com.dk.games.jcgame.core.rule;

import com.dk.games.jcgame.model.IBattleChar;
import com.dk.games.jcgame.model.action.Action;

public interface BattleRule {

    /**
     * Informs if the char can processed to attack or miss
     *
     * @param battleChar
     * @param dice a random number from 1 to 6
     * @return True - attack, False - 0 miss
     */
    boolean canAttack(IBattleChar battleChar, int dice);

    /**
     * Informs if the char can dodge the attack
     *
     * @param battleChar
     * @param action the attack
     * @param dice a random number from 1 to 6
     * @return true - 1 dodge, False - receive attack
     */
    boolean canDodge(Action action, IBattleChar battleChar, int dice);

}
