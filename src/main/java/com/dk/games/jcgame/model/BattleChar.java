package com.dk.games.jcgame.model;

import com.dk.games.jcgame.constant.GameConstants;
import com.dk.games.jcgame.core.rule.BattleRule;
import com.dk.games.jcgame.core.rule.BattleRuleImpl;
import com.dk.games.jcgame.model.action.Action;
import com.dk.games.jcgame.model.action.MagicAction;
import com.dk.games.jcgame.model.exception.NoStaminaPointsException;
import com.dk.games.jcgame.core.rule.LevelUpRule;
import com.dk.games.jcgame.core.rule.LevelUpRuleImpl;
import com.dk.games.jcgame.utils.CloneUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class BattleChar extends BaseChar implements Serializable, IBattleChar {
    private static final int BASE_HEALTH_POINTS = 10;
    private static final int BASE_STAMINA_POINTS = 5;
    private static final long serialVersionUID = -2144793771790351475L;

    private int level;

    private int health = BASE_HEALTH_POINTS;
    private int maxHealth = health;
    private int stamina = BASE_STAMINA_POINTS;
    private int maxStamina = stamina;

    private Map<Skill, SkillLevel> skills;
    private SortedSet<Action> actions;
    private SortedSet<MagicAction> magicActions;
    private LevelUpRule levelUpRule;
    private BattleRule battleRule;

    private BattleChar() {
        skills = new HashMap<>();
        actions = new TreeSet<>();
        magicActions = new TreeSet<>();

        levelUpRule = new LevelUpRuleImpl();
        battleRule = new BattleRuleImpl();
    }

    @Override
    public int getMaxHealth() {
        return maxHealth;
    }

    @Override
    public int getMaxStamina() {
        return maxStamina;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public int getStamina() {
        return stamina;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public SortedSet<Action> getActions() {
        return actions;
    }

    @Override
    public SortedSet<MagicAction> getMagicActions() {
        return magicActions;
    }

    @Override
    public Map<Skill, SkillLevel> getSkills() {
        return skills;
    }

    @Override
    public SkillLevel getSkill(Skill skill) {
        return skills.get(skill);
    }


    public void setHealth(int health) {
        this.health = health;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public LevelUpRule getLevelUpRule() {
        return levelUpRule;
    }

    public void setLevelUpRule(LevelUpRule levelUpRule) {
        this.levelUpRule = levelUpRule;
    }

    public void setSkills(Map<Skill, SkillLevel> skills) {
        this.skills = skills;
    }


    @Override
    public void fireLevelUp(int level) {
        setLevel(level);

        this.health = levelUpRule.getHitPoints(BASE_HEALTH_POINTS, level);
        this.stamina = levelUpRule.getHitPoints(BASE_STAMINA_POINTS, level);
        this.maxHealth = health;
        this.maxStamina = stamina;

        this.skills.forEach((k,v) -> v.setPoints(levelUpRule.getHitPoints(v.getStartPoints(), level)) );
        this.actions.forEach(v -> v.setHitPoints(levelUpRule.getHitPoints(v.getStartHitPoints(), level)) );
        this.magicActions.forEach(v -> v.setHitPoints(levelUpRule.getHitPoints(v.getStartHitPoints(), level)) );
    }

    @Override
    public void fireAfterBattle() {
        setStamina(getMaxStamina());
        setHealth(getMaxHealth());
    }

    @Override
    public void addSkill(Skill skill, int hitPoints) {
        SkillLevel skillLevel = skills.get(skill);
        if (skillLevel == null) {
            this.skills.put(skill, new SkillLevel(skill, hitPoints));
            return;
        }

        skillLevel.setPoints(hitPoints);
    }

    @Override
    public void addAction(Action action) {
        this.actions.add(action);
    }

    @Override
    public void addMagicAction(MagicAction action) {
        this.magicActions.add(action);
    }

    @Override
    public int receiveAttack(Action action, int damage) {
        // dodge the attack
        if (battleRule.canDodge(action,this, rollDice())) {
            return 0;
        }

        int receivedDamage = damage;
        // receive the attack
        SkillLevel resistance = skills.get(Skill.RESISTANCE);
        if (resistance != null) {
            if (resistance.getPoints() > damage) {
                receivedDamage = 1;
            } else {
                receivedDamage = damage - resistance.getPoints();
            }
        }

        this.health -= receivedDamage;

        if (!isAlive()) {
            this.health = 0;
        }

        return receivedDamage;
    }

    @Override
    public int doAttack(Action action) {
        if (battleRule.canAttack(this, rollDice())) {
            if (action instanceof MagicAction) {
                // magical attack
                MagicAction ac = (MagicAction) action;
                if (ac.getStaminaPoints() > getStamina()) {
                    throw new NoStaminaPointsException("Your char does not have enough stamina to do this action.");
                }
                this.stamina -= ac.getStaminaPoints();
                return action.getHitPoints();
            }

            // physical attack
            SkillLevel strength = skills.get(Skill.STRENGTH);
            if (strength != null) {
                return action.getHitPoints() + strength.getPoints();
            }

            return action.getHitPoints();
        }

        return 0;
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public boolean canFlee() {
        SkillLevel intelligence = getSkills().get(Skill.INTELLIGENCE);
        if (intelligence == null) {
            return rollDice() >= GameConstants.DICE_SUCCESS;
        }

        return intelligence.getPoints() + rollDice() >= GameConstants.DICE_SUCCESS;
    }

    int rollDice() {
        return new Random().nextInt(6) + 1;
    }


    @Override
    public BattleChar copy() {
        try {
            BattleChar chars = (BattleChar) super.clone();
            chars.skills = CloneUtils.deepCloneMap(skills);
            chars.actions = CloneUtils.deepCloneSortedSet(actions);

            chars.magicActions = magicActions.stream()
                    .map(e -> (MagicAction) e.copy())
                    .collect(Collectors.toCollection(TreeSet::new));
            return chars;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public static BattleCharBuilder builder() {
        return new BattleCharBuilder();
    }

    public static class BattleCharBuilder {
        private BattleChar character;
        private int level;

        public BattleCharBuilder() {
            this.character = new BattleChar();
        }

        public BattleCharBuilder name(String name) {
            character.setName(name);
            return this;
        }

        public BattleCharBuilder level(int level) {
            this.level = level;
            return this;
        }

        public BattleCharBuilder hero(boolean hero) {
            character.setHero(hero);
            return this;
        }

        public BattleCharBuilder addSkill(Skill skill, int hitPoints) {
            character.addSkill(skill, hitPoints);
            return this;
        }

        public BattleCharBuilder addAction(Action action) {
            character.addAction(action);
            return this;
        }

        public BattleCharBuilder addMagicAction(MagicAction action) {
            character.addMagicAction(action);
            return this;
        }

        public BattleChar build() {
            character.fireLevelUp(level);
            return character;
        }

    }
}
