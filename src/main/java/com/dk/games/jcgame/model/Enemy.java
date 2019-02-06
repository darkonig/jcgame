package com.dk.games.jcgame.model;

import com.dk.games.jcgame.model.action.Action;
import com.dk.games.jcgame.model.action.MagicAction;

import java.util.Map;
import java.util.SortedSet;

public class Enemy implements IBattleChar {

    private static final long serialVersionUID = 6604757259423342856L;
    private IBattleChar character;
    private String name;
    private int expPoints;

    private Enemy() {
        super();
    }

    private Enemy(IBattleChar character, String name, int expPoints) {
        this();
        this.character = character;
        this.name = name;
        this.expPoints = expPoints;
    }

    private IBattleChar getCharacter() {
        return character;
    }

    public int getExpPoints() {
        return expPoints;
    }

    public void setExpPoints(int expPoints) {
        this.expPoints = expPoints;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean isHero() {
        return false;
    }

    @Override
    public boolean isAlive() {
        return this.getCharacter().isAlive();
    }

    @Override
    public boolean canFlee() {
        return false;
    }

    @Override
    public int receiveAttack(Action action, int damage) {
        return this.getCharacter().receiveAttack(action, damage);
    }

    @Override
    public int doAttack(Action action) {
        return this.getCharacter().doAttack(action);
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
    public Map<Skill, SkillLevel> getSkills() {
        return null;
    }

    @Override
    public SkillLevel getSkill(Skill skill) {
        return this.getCharacter().getSkill(skill);
    }

    @Override
    public int getLevel() {
        return this.getCharacter().getLevel();
    }

    @Override
    public void fireLevelUp(int level) {
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
    public Enemy copy() {
        try {
            Enemy e = (Enemy) super.clone();
            e.character = (IBattleChar) character.copy();

            return e;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public static Enemy create(IBattleChar character, String name, int expPoints) {
        return new Enemy(character, name, expPoints);
    }
}
