package com.dk.games.jcgame.model;

import com.dk.games.jcgame.model.action.Action;
import com.dk.games.jcgame.model.action.ActionCrazyHowl;
import com.dk.games.jcgame.model.action.ActionStrongBite;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerTest {

    private Player player;

    @Before
    public void setUp() {
        player = Player.create("Frodo"
                , BattleChar.builder()
                        .name("Human")
                        .level(0)
                        .hero(true)
                        .addSkill(Skill.INTELLIGENCE, 2)
                        .addAction(new ActionStrongBite())
                        .addMagicAction(new ActionCrazyHowl())
                    .build());
    }

    @Test
    public void addExperience() {
        Action action = player.getActions().first();
        Action fire = player.getActions().first();

        int lvl1 = Math.round(2 * (float) Math.pow(1.2, 1));
        int lvl2 = Math.round(2 * (float) Math.pow(1.2, 2));
        int lvl3 = Math.round(2 * (float) Math.pow(1.2, 3));
        int lvl5 = Math.round(2 * (float) Math.pow(1.2, 5));

        int actionLvl1 = Math.round(action.getStartHitPoints() * (float) Math.pow(1.2, 1));
        int actionLvl2 = Math.round(action.getStartHitPoints() * (float) Math.pow(1.2, 2));
        int actionLvl3 = Math.round(action.getStartHitPoints() * (float) Math.pow(1.2, 3));
        int actionLvl5 = Math.round(action.getStartHitPoints() * (float) Math.pow(1.2, 5));

        int fireLvl1 = Math.round(fire.getStartHitPoints() * (float) Math.pow(1.2, 1));
        int fireLvl2 = Math.round(fire.getStartHitPoints() * (float) Math.pow(1.2, 2));
        int fireLvl3 = Math.round(fire.getStartHitPoints() * (float) Math.pow(1.2, 3));
        int fireLvl5 = Math.round(fire.getStartHitPoints() * (float) Math.pow(1.2, 5));

        player.addExperience(10);
        assertThat(player.getLevel()).isEqualTo(1);
        assertThat(player.getSkill(Skill.INTELLIGENCE).getPoints()).isEqualTo(lvl1);
        assertThat(action.getHitPoints()).isEqualTo(actionLvl1);
        assertThat(fire.getHitPoints()).isEqualTo(fireLvl1);

        player.addExperience(20);
        assertThat(player.getLevel()).isEqualTo(2);
        assertThat(player.getSkill(Skill.INTELLIGENCE).getPoints()).isEqualTo(lvl2);
        assertThat(action.getHitPoints()).isEqualTo(actionLvl2);
        assertThat(fire.getHitPoints()).isEqualTo(fireLvl2);

        player.addExperience(player.getLvlMaxExp() - player.getExperience());
        assertThat(player.getLevel()).isEqualTo(3);
        assertThat(player.getSkill(Skill.INTELLIGENCE).getPoints()).isEqualTo(lvl3);
        assertThat(action.getHitPoints()).isEqualTo(actionLvl3);
        assertThat(fire.getHitPoints()).isEqualTo(fireLvl3);

        player.addExperience(100);
        assertThat(player.getLevel()).isEqualTo(5);
        assertThat(player.getSkill(Skill.INTELLIGENCE).getPoints()).isEqualTo(lvl5);
        assertThat(action.getHitPoints()).isEqualTo(actionLvl5);
        assertThat(fire.getHitPoints()).isEqualTo(fireLvl5);
    }
}