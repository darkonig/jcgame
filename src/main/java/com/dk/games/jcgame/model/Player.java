package com.dk.games.jcgame.model;

import com.dk.games.jcgame.model.action.Action;
import com.dk.games.jcgame.model.action.MagicAction;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;
import java.util.Map;
import java.util.SortedSet;

public class Player implements Serializable, IBattleChar {
    private static final int BASE_EXP = 10;
    private static final long serialVersionUID = -5113537062896012888L;

    private String name;
    private IBattleChar character;
    private int experience;
    private int lvlMaxExp;

    public static Player create(String name, IBattleChar character) {
        return create(name, character, 0);
    }

    public static Player create(String name, IBattleChar character, int experience) {
        return new Player(name, character, experience);
    }

    private Player() {
        super();
    }

    private Player(String name, IBattleChar character, int experience) {
        this.name = name;
        this.character = character;
        this.experience = experience;
        fireLevelUp(0);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean isHero() {
        return true;
    }

    public void setName(String name) {
        this.name = name;
    }

    private IBattleChar getCharacter() {
        return character;
    }

    public int getExperience() {
        return experience;
    }

    public int getLvlMaxExp() {
        return lvlMaxExp;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void addExperience(int experience) {
        this.experience += experience;
        int exp = this.experience - lvlMaxExp;
        if (exp >= 0) {
            this.experience = 0;
            fireLevelUp(getLevel() + 1);
            addExperience(exp);
        }
    }

    @Override
    public int getLevel() {
        return this.getCharacter().getLevel();
    }

    @Override
    public void fireLevelUp(int level) {
        this.lvlMaxExp = BASE_EXP + level * BASE_EXP;
        this.getCharacter().fireLevelUp(level);
    }

    @Override
    public void fireAfterBattle() {
        this.getCharacter().fireAfterBattle();
    }

    @Override
    public void addSkill(Skill skill, int hitPoints) {
        this.getCharacter().addSkill(skill, hitPoints);
    }

    @Override
    public void addAction(Action action) {
        this.getCharacter().addAction(action);
    }

    @Override
    public void addMagicAction(MagicAction action) {
        this.getCharacter().addMagicAction(action);
    }

    @Override
    public int doAttack(Action action) {
        return this.getCharacter().doAttack(action);
    }

    @Override
    public int receiveAttack(Action action, int damage) {
        return this.getCharacter().receiveAttack(action, damage);
    }

    @Override
    public boolean isAlive() {
        return this.getCharacter().isAlive();
    }

    @Override
    public Map<Skill, SkillLevel> getSkills() {
        return this.getCharacter().getSkills();
    }

    @Override
    public SkillLevel getSkill(Skill skill) {
        return this.getCharacter().getSkill(skill);
    }

    @Override
    public int getMaxHealth() {
        return this.getCharacter().getMaxHealth();
    }

    @Override
    public int getMaxStamina() {
        return this.getCharacter().getMaxStamina();
    }

    @Override
    public int getHealth() {
        return this.getCharacter().getHealth();
    }

    @Override
    public int getStamina() {
        return this.getCharacter().getStamina();
    }

    @Override
    public SortedSet<Action> getActions() {
        return this.getCharacter().getActions();
    }

    @Override
    public SortedSet<MagicAction> getMagicActions() {
        return this.getCharacter().getMagicActions();
    }

    @Override
    public boolean canFlee() {
        return this.getCharacter().canFlee();
    }

    @Override
    public IBattleChar copy() {
        throw new NotImplementedException();
    }
}
