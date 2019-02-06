package com.dk.games.jcgame.model;

import com.dk.games.jcgame.model.action.Action;
import com.dk.games.jcgame.model.action.ActionFire;
import com.dk.games.jcgame.model.action.ActionPunch;
import com.dk.games.jcgame.model.action.ActionStrongPunch;
import com.dk.games.jcgame.model.exception.NoStaminaPointsException;
import com.dk.games.jcgame.core.rule.LevelUpRuleImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BattleCharTest {

    private BattleChar character;

    @Before
    public void setUp() {
        character = spy(BattleChar.builder()
                .name("Human")
                .level(0)
                .hero(true)
                .addSkill(Skill.INTELLIGENCE, 0)
                .addAction(new ActionStrongPunch())
                .addMagicAction(new ActionFire())
            .build());
    }

    @Test
    public void addSkill_add() {
        character.addSkill(Skill.ABILITY, 2);

        assertThat(character.getSkills().size()).isEqualTo(2);
    }

    @Test
    public void addSkill_update() {
        character.addSkill(Skill.INTELLIGENCE, 4);

        assertThat(character.getSkills().size()).isEqualTo(1);
        assertThat(character.getSkills().get(Skill.INTELLIGENCE).getPoints()).isEqualTo(4);
    }

    @Test
    public void addAction_add() {
        character.addAction(new ActionPunch());

        assertThat(character.getActions().size()).isEqualTo(2);
    }

    @Test
    public void addAction_noAdd() {
        character.addAction(new ActionStrongPunch());

        assertThat(character.getActions().size()).isEqualTo(1);
    }

    @Test
    public void receiveAttack_success() {
        int damage = 5;
        int health = character.getHealth();

        when(character.rollDice()).thenReturn(3);

        assertThat(character.receiveAttack(null, damage)).isGreaterThan(0);
        assertThat(character.getHealth()).isEqualTo(health - damage);
    }

    @Test
    public void receiveAttack_successKill() {
        int damage = 5;
        int health = 4;

        character.setHealth(health);
        when(character.rollDice()).thenReturn(3);

        assertThat(character.receiveAttack(null, damage)).isGreaterThan(0);
        assertThat(character.getHealth()).isEqualTo(0);
        assertThat(character.isAlive()).isFalse();
    }

    @Test
    public void receiveAttack_dodge() {
        int damage = 5;
        int health = character.getHealth();

        when(character.rollDice()).thenReturn(5);

        assertThat(character.receiveAttack(null, damage)).isEqualTo(0);
        assertThat(character.getHealth()).isEqualTo(health);
    }

    @Test
    public void receiveAttack_successReducedDamage() {
        int resistance = 1;
        int damage = 5;
        int health = character.getHealth();

        character.addSkill(Skill.RESISTANCE, resistance);
        when(character.rollDice()).thenReturn(3);

        assertThat(character.receiveAttack(null, damage)).isGreaterThan(0);
        assertThat(character.getHealth()).isEqualTo(health - (damage - resistance));
    }

    @Test
    public void receiveAttack_successDamage1Point() {
        int resistance = 3;
        int damage = 2;
        int health = character.getHealth();

        character.addSkill(Skill.RESISTANCE, resistance);
        when(character.rollDice()).thenReturn(3);

        assertThat(character.receiveAttack(null, damage)).isGreaterThan(0);
        assertThat(character.getHealth()).isEqualTo(health - 1);
    }

    @Test
    public void doAttack_success() {
        Action action = character.getActions().first();

        when(character.rollDice()).thenReturn(4);

        assertThat(character.doAttack(action)).isEqualTo(action.getHitPoints());
    }

    @Test
    public void doAttack_successExtraStr() {
        int str = 1;
        character.addSkill(Skill.STRENGTH, str);
        Action action = character.getActions().first();

        when(character.rollDice()).thenReturn(4);

        assertThat(character.doAttack(action)).isEqualTo(action.getHitPoints() + str);
    }

    @Test
    public void doAttack_miss() {
        Action action = character.getActions().first();

        when(character.rollDice()).thenReturn(3);

        assertThat(character.doAttack(action)).isEqualTo(2);
    }

    @Test
    public void doMagicAttack_success() {
        Action action = character.getMagicActions().first();

        when(character.rollDice()).thenReturn(4);

        assertThat(character.doAttack(action)).isEqualTo(action.getHitPoints());
    }

    @Test(expected = NoStaminaPointsException.class)
    public void doMagicAttack_error() {
        character.setStamina(1);
        Action action = character.getMagicActions().first();

        when(character.rollDice()).thenReturn(4);

        character.doAttack(action);
    }

    @Test
    public void fireLevelUp_success() {
        Action action = character.getActions().first();
        Action action2 = new ActionPunch();
        Action fire = character.getActions().first();

        int lvl1 = Math.round(2 * (float) Math.pow(1.2, 1));
        int lvl2 = Math.round(2 * (float) Math.pow(1.2, 2));
        int lvl3 = Math.round(2 * (float) Math.pow(1.2, 3));
        int lvl5 = Math.round(2 * (float) Math.pow(1.2, 5));

        int actionLvl1 = Math.round(action.getStartHitPoints() * (float) Math.pow(1.2, 1));
        int actionLvl2 = Math.round(action.getStartHitPoints() * (float) Math.pow(1.2, 2));
        int actionLvl3 = Math.round(action.getStartHitPoints() * (float) Math.pow(1.2, 3));
        int actionLvl5 = Math.round(action.getStartHitPoints() * (float) Math.pow(1.2, 5));

        int action2Lvl1 = Math.round(action2.getStartHitPoints() * (float) Math.pow(1.2, 1));
        int action2Lvl2 = Math.round(action2.getStartHitPoints() * (float) Math.pow(1.2, 2));
        int action2Lvl3 = Math.round(action2.getStartHitPoints() * (float) Math.pow(1.2, 3));
        int action2Lvl5 = Math.round(action2.getStartHitPoints() * (float) Math.pow(1.2, 5));

        int fireLvl1 = Math.round(fire.getStartHitPoints() * (float) Math.pow(1.2, 1));
        int fireLvl2 = Math.round(fire.getStartHitPoints() * (float) Math.pow(1.2, 2));
        int fireLvl3 = Math.round(fire.getStartHitPoints() * (float) Math.pow(1.2, 3));
        int fireLvl5 = Math.round(fire.getStartHitPoints() * (float) Math.pow(1.2, 5));

        character.setLevelUpRule(new LevelUpRuleImpl());
        character.addSkill(Skill.STRENGTH, 2);
        character.addAction(action2);

        character.fireLevelUp(1);
        assertThat(character.getSkill(Skill.STRENGTH).getPoints()).isEqualTo(lvl1);
        assertThat(action.getHitPoints()).isEqualTo(actionLvl1);
        assertThat(action2.getHitPoints()).isEqualTo(action2Lvl1);
        assertThat(fire.getHitPoints()).isEqualTo(fireLvl1);

        character.fireLevelUp(2);
        assertThat(character.getSkill(Skill.STRENGTH).getPoints()).isEqualTo(lvl2);
        assertThat(action.getHitPoints()).isEqualTo(actionLvl2);
        assertThat(action2.getHitPoints()).isEqualTo(action2Lvl2);
        assertThat(fire.getHitPoints()).isEqualTo(fireLvl2);

        character.fireLevelUp(3);
        assertThat(character.getSkill(Skill.STRENGTH).getPoints()).isEqualTo(lvl3);
        assertThat(action.getHitPoints()).isEqualTo(actionLvl3);
        assertThat(action2.getHitPoints()).isEqualTo(action2Lvl3);
        assertThat(fire.getHitPoints()).isEqualTo(fireLvl3);

        character.fireLevelUp(5);
        assertThat(character.getSkill(Skill.STRENGTH).getPoints()).isEqualTo(lvl5);
        assertThat(action.getHitPoints()).isEqualTo(actionLvl5);
        assertThat(action2.getHitPoints()).isEqualTo(action2Lvl5);
        assertThat(fire.getHitPoints()).isEqualTo(fireLvl5);
    }
}