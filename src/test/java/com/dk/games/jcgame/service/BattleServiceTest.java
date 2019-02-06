package com.dk.games.jcgame.service;

import com.dk.games.jcgame.model.*;
import com.dk.games.jcgame.model.action.Action;
import com.dk.games.jcgame.model.action.ActionFire;
import com.dk.games.jcgame.model.action.ActionStrongPunch;
import com.dk.games.jcgame.core.scene.BattleScene;
import com.dk.games.jcgame.service.exception.LoadException;
import com.dk.games.jcgame.service.impl.BattleServiceImpl;
import com.dk.games.jcgame.service.impl.SceneServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BattleServiceTest {

    private BattleService service;
    private SceneService sceneService;

    @Spy
    private Player hero;
    @Spy
    private Enemy enemy;

    @Before
    public void setUp() {
        sceneService = new SceneServiceImpl(null);
        service = new BattleServiceImpl(sceneService);

        IBattleChar heroChar = BattleChar.builder()
                .name("Human")
                .addSkill(Skill.STRENGTH, 2)
                .addSkill(Skill.ABILITY, 2)
                .addSkill(Skill.INTELLIGENCE, 3)
                .addSkill(Skill.RESISTANCE, 1)
                .addAction(new ActionStrongPunch())
                .addMagicAction(new ActionFire())
                .hero(true)
                .build();
        IBattleChar enemyChar = BattleChar.builder()
                .name("Elf")
                .addSkill(Skill.STRENGTH, 2)
                .addSkill(Skill.ABILITY, 2)
                .addSkill(Skill.INTELLIGENCE, 3)
                .addSkill(Skill.RESISTANCE, 1)
                .addAction(new ActionStrongPunch())
                .build();

        hero = spy(Player.create("Hero", heroChar));

        enemy = spy(Enemy.create(enemyChar, enemyChar.getName(), 10));
    }

    @Test
    public void begin() throws LoadException {
        BattleSceneTurn begin = service.begin(hero, enemy);

        System.out.println(begin.getScene());
        assertThat(begin.getScene()).isNotNull();
        assertThat(begin.getScene().getScene()).contains(enemy.getName());
    }

    @Test
    public void chooseAction() throws LoadException {
        BattleSceneTurn begin = service.begin(hero, enemy);
        BattleScene actions = service.chooseAction(begin.getBattleScene(), false);

        System.out.println(actions.getScene());
        assertThat(actions.getScene()).isNotNull();
        assertThat(actions.getScene().getScene()).contains(enemy.getName());
        assertThat(actions.getScene().getScene()).contains(hero.getActions().first().getName());
    }

    @Test
    public void chooseAction_magic() throws LoadException {
        BattleSceneTurn begin = service.begin(hero, enemy);
        BattleScene actions = service.chooseAction(begin.getBattleScene(), true);

        System.out.println(actions.getScene());
        assertThat(actions.getScene()).isNotNull();
        assertThat(actions.getScene().getScene()).contains(enemy.getName());
        assertThat(actions.getScene().getScene()).contains(hero.getMagicActions().first().getName());
    }

    @Test
    public void doPlayerInput_gotoActions() throws LoadException {
        BattleSceneTurn begin = service.begin(hero, enemy);
        BattleSceneTurn actions = service.execPlayerInput(begin.getBattleScene(), "1");

        System.out.println(actions.getScene());
        assertThat(actions.getScene()).isNotNull();
        assertThat(actions.isPlayerTurn()).isTrue();
        assertThat(actions.isFinished()).isFalse();
        assertThat(actions.getScene().getScene()).contains(enemy.getName());
        assertThat(actions.getScene().getScene()).contains(hero.getActions().first().getName());
    }

    @Test
    public void attack_damage() throws LoadException {
        doReturn(5).when(hero).doAttack(any(Action.class));
        doReturn(5).when(enemy).receiveAttack(any(Action.class), anyInt());

        BattleSceneTurn begin = service.begin(hero, enemy);
        BattleSceneTurn actions = service.execPlayerInput(begin.getBattleScene(), "1");
        BattleSceneTurn result = service.execPlayerInput(actions.getBattleScene(), "1");

        System.out.println(result.getScene());
        assertThat(result.getScene()).isNotNull();
        assertThat(result.isPlayerTurn()).isFalse();
        assertThat(result.getScene().getScene()).contains(enemy.getName());
        assertThat(result.getBattleScene().getAttackInfo2()).contains("5 damage");
    }

    @Test
    public void attack_won() throws LoadException {
        doReturn(5).when(hero).doAttack(any(Action.class));
        doReturn(10).when(enemy).receiveAttack(any(Action.class), anyInt());
        doReturn(false).when(enemy).isAlive();

        BattleSceneTurn begin = service.begin(hero, enemy);
        BattleSceneTurn actions = service.execPlayerInput(begin.getBattleScene(), "1");
        BattleSceneTurn result = service.execPlayerInput(actions.getBattleScene(), "1");

        System.out.println(result.getScene());
        assertThat(result.getScene()).isNotNull();
        assertThat(result.isFinished()).isTrue();
        assertThat(result.getScene().getScene()).contains(enemy.getName());
        assertThat(result.getBattleScene().getAttackInfo2()).contains("you won");
    }

    @Test
    public void attack_miss() throws LoadException {
        doReturn(0).when(hero).doAttack(any(Action.class));

        BattleSceneTurn begin = service.begin(hero, enemy);
        BattleSceneTurn actions = service.execPlayerInput(begin.getBattleScene(), "1");
        BattleSceneTurn result = service.execPlayerInput(actions.getBattleScene(), "1");

        System.out.println(result.getScene());
        assertThat(result.getScene()).isNotNull();
        assertThat(result.getScene().getScene()).contains(enemy.getName());
        assertThat(result.getBattleScene().getAttackInfo2()).contains("miss");
    }

    @Test
    public void attack_enemyDodge() throws LoadException {
        doReturn(5).when(hero).doAttack(any(Action.class));
        doReturn(0).when(enemy).receiveAttack(any(Action.class), anyInt());

        BattleSceneTurn begin = service.begin(hero, enemy);
        BattleSceneTurn actions = service.execPlayerInput(begin.getBattleScene(), "1");
        BattleSceneTurn result = service.execPlayerInput(actions.getBattleScene(), "1");

        System.out.println(result.getScene());
        assertThat(result.getScene()).isNotNull();
        assertThat(result.getScene().getScene()).contains(enemy.getName());
        assertThat(result.getBattleScene().getAttackInfo2()).contains("dodge");
    }

    @Test
    public void enemyAttack_damage() throws LoadException {
        BattleSceneTurn begin = service.begin(hero, enemy);
        BattleSceneTurn actions = service.execPlayerInput(begin.getBattleScene(), "1");
        BattleSceneTurn attackScene = service.execPlayerInput(actions.getBattleScene(), "1");

        doReturn(5).when(enemy).doAttack(any(Action.class));
        doReturn(5).when(hero).receiveAttack(any(Action.class), anyInt());

        BattleSceneTurn result = service.execEnemyTurn(attackScene.getBattleScene());

        System.out.println(result.getScene());
        assertThat(result.getScene()).isNotNull();
        assertThat(result.getBattleScene().getAttackInfo2()).contains("You took 5 damage");
    }

    @Test
    public void enemyAttack_miss() throws LoadException {
        BattleSceneTurn begin = service.begin(hero, enemy);
        BattleSceneTurn actions = service.execPlayerInput(begin.getBattleScene(), "1");
        BattleSceneTurn attackScene = service.execPlayerInput(actions.getBattleScene(), "1");

        doReturn(0).when(enemy).doAttack(any(Action.class));

        BattleSceneTurn result = service.execEnemyTurn(attackScene.getBattleScene());

        System.out.println(result.getScene());
        assertThat(result.getScene()).isNotNull();
        assertThat(result.getBattleScene().getAttackInfo2()).contains("miss");
    }

    @Test
    public void enemyAttack_dodge() throws LoadException {
        BattleSceneTurn begin = service.begin(hero, enemy);
        BattleSceneTurn actions = service.execPlayerInput(begin.getBattleScene(), "1");
        BattleSceneTurn attackScene = service.execPlayerInput(actions.getBattleScene(), "1");

        doReturn(5).when(enemy).doAttack(any(Action.class));
        doReturn(0).when(hero).receiveAttack(any(Action.class), anyInt());

        BattleSceneTurn result = service.execEnemyTurn(attackScene.getBattleScene());

        System.out.println(result.getScene());
        assertThat(result.getScene()).isNotNull();
        assertThat(result.getBattleScene().getAttackInfo2()).contains("You dodge");
    }

    @Test
    public void enemyAttack_youLose() throws LoadException {
        BattleSceneTurn begin = service.begin(hero, enemy);
        BattleSceneTurn actions = service.execPlayerInput(begin.getBattleScene(), "1");
        BattleSceneTurn attackScene = service.execPlayerInput(actions.getBattleScene(), "1");

        doReturn(5).when(enemy).doAttack(any(Action.class));
        doReturn(10).when(hero).receiveAttack(any(Action.class), anyInt());
        doReturn(false).when(hero).isAlive();

        BattleSceneTurn result = service.execEnemyTurn(attackScene.getBattleScene());

        System.out.println(result.getScene());
        assertThat(result.getScene()).isNotNull();
        assertThat(result.getBattleScene().getAttackInfo2()).contains("you lose");
    }

    @Test
    public void flee_success() throws LoadException {
        when(hero.canFlee()).thenReturn(true);

        BattleSceneTurn begin = service.begin(hero, enemy);
        BattleSceneTurn result = service.execPlayerInput(begin.getBattleScene(), "3");

        System.out.println(result.getScene());
        assertThat(result.getScene()).isNotNull();
        assertThat(result.getBattleScene().getAttackInfo2()).contains("You fled");
    }

    @Test
    public void flee_fail() throws LoadException {
        when(hero.canFlee()).thenReturn(false);

        BattleSceneTurn begin = service.begin(hero, enemy);
        BattleSceneTurn result = service.execPlayerInput(begin.getBattleScene(), "3");

        System.out.println(result.getScene());
        assertThat(result.getScene()).isNotNull();
        assertThat(result.getBattleScene().getAttackInfo2()).contains("Oops");
    }
}