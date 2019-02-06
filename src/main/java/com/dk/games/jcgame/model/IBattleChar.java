package com.dk.games.jcgame.model;

import com.dk.games.jcgame.model.action.Action;
import com.dk.games.jcgame.model.action.MagicAction;

import java.util.Map;
import java.util.SortedSet;

public interface IBattleChar extends Char {

    int getMaxHealth();

    int getMaxStamina();

    int getHealth();

    int getStamina();

    SortedSet<Action> getActions();

    SortedSet<MagicAction> getMagicActions();

    Map<Skill, SkillLevel> getSkills();

    SkillLevel getSkill(Skill skill);

    int getLevel();

    void fireLevelUp(int level);

    void fireAfterBattle();

    void addSkill(Skill skill, int hitPoints);

    void addAction(Action action);

    void addMagicAction(MagicAction action);


    /**
     * Receives the attack
     *
     * @param damage the attack damage
     * @return > 0 - if the attack has been well succeeded
     *         0 - if the char dodges the attack
     */
    int receiveAttack(Action action, int damage);

    /**
     * Gets attack damage
     *
     * @param action the attack that will be performed
     * @return attack damage
     */
    int doAttack(Action action);

    /**
     * Gets if the char is alive
     *
     * @return true - Char is alive
     */
    boolean isAlive();

    /**
     * Gets if the char is able to flee the battle
     *
     * @return True - if the you can flee
     */
    boolean canFlee();

}
