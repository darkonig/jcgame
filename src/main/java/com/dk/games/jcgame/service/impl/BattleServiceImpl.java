package com.dk.games.jcgame.service.impl;

import com.dk.games.jcgame.model.*;
import com.dk.games.jcgame.model.action.Action;
import com.dk.games.jcgame.model.action.MagicAction;
import com.dk.games.jcgame.core.scene.BattleScene;
import com.dk.games.jcgame.core.scene.Scene;
import com.dk.games.jcgame.service.BattleSceneTurn;
import com.dk.games.jcgame.service.BattleService;
import com.dk.games.jcgame.service.SceneService;
import com.dk.games.jcgame.service.exception.LoadException;

import java.util.Random;
import java.util.SortedSet;

public class BattleServiceImpl implements BattleService {

    private SceneService service;

    public BattleServiceImpl(SceneService service) {
        this.service = service;
    }

    @Override
    public BattleSceneTurn begin(Player hero, Enemy enemy) throws LoadException {
        Scene scene = service.loadScene(null,"battle");
        return new BattleSceneTurn(true, new BattleScene(scene, hero, enemy), false);
    }

    @Override
    public BattleScene chooseAction(BattleScene parent, boolean magic) throws LoadException {
        Scene scene = service.loadScene(parent.getScene().getName(),"battle-actions");
        return new BattleScene(parent, scene, !magic, magic);
    }

    @Override
    public BattleSceneTurn execPlayerInput(BattleScene scene, String input) throws LoadException {
        BattleScene.BattleSceneAction battleSceneAction = scene.setPlayerInput(input);

        if (battleSceneAction != null) {
            switch (battleSceneAction.getActionType()) {
                case BattleScene.BattleSceneAction.TYPE_ACTION:

                    if (battleSceneAction.getAction() instanceof MagicAction) {
                        MagicAction maction = (MagicAction) battleSceneAction.getAction();
                        if (maction.getStaminaPoints() > scene.getHero().getStamina()) {
                            scene.getParent().setAttackInfo("You do not have enough stamina");
                            scene.getParent().setAttackInfo2("to execute this attack.");

                            scene.getParent().update();

                            return new BattleSceneTurn(true, scene.getParent(), false);
                        }
                    }

                    int damage = scene.getHero().doAttack(battleSceneAction.getAction());

                    scene.getParent().resetAttackInfos();

                    boolean finish = false;
                    if (damage == 0) {
                        // miss
                        scene.getParent().setAttackInfo2("You miss!");
                    } else {
                        scene.getParent().setAttackInfo(String.format("You attack %s!", battleSceneAction.getAction().getName()));

                        int hit = scene.getEnemy().receiveAttack(battleSceneAction.getAction(), damage);
                        if(hit > 0) {
                            // hit
                            scene.getParent().setAttackInfo2(String.format("%s took %d damage!", scene.getEnemy().getName(), hit));
                        } else {
                            // dodge
                            scene.getParent().setAttackInfo2(String.format("%s dodged from your attack!", scene.getEnemy().getName()));
                        }

                        if (!scene.getEnemy().isAlive()) {
                            finish = true;
                            scene.getParent().setAttackInfo2(String.format("%s took %d damage, you won the battle.", scene.getEnemy().getName(), hit));
                            scene.getHero().addExperience(scene.getEnemy().getExpPoints());
                        }
                    }

                    scene.getParent().update();

                    return new BattleSceneTurn(false, scene.getParent(), finish);
                case BattleScene.BattleSceneAction.TYPE_SCENE_FLEE:
                    boolean flee = scene.getHero().canFlee();
                    scene.resetAttackInfos();

                    if (flee) {
                        scene.setAttackInfo2("You fled the battle.");
                    } else {
                        scene.setAttackInfo2("Oops! You couldn't flee the battle.");
                    }

                    scene.update();

                    return new BattleSceneTurn(false, scene, flee);
                case BattleScene.BattleSceneAction.TYPE_SCENE_ACTIONS:
                    BattleScene battleScene = chooseAction(scene, false);
                    return new BattleSceneTurn(true, battleScene, false);
                case BattleScene.BattleSceneAction.TYPE_SCENE_MAGICAL_ACTIONS:
                    BattleScene battleScene1 = chooseAction(scene, true);
                    return new BattleSceneTurn(true, battleScene1, false);
                case BattleScene.BattleSceneAction.TYPE_SCENE_BEGIN:
                    return new BattleSceneTurn(true, scene.getParent(), false);
            }
        }

        return new BattleSceneTurn(true, scene, false);
    }

    @Override
    public BattleSceneTurn execEnemyTurn(BattleScene scene) {
        SortedSet<MagicAction> magicActions = scene.getEnemy().getMagicActions();
        Action[] actions = scene.getEnemy().getActions()
                .toArray(new Action[]{});

        Action action = null;
        int damage;
        if (!magicActions.isEmpty() && randomNum(2) > 1) {
            MagicAction[] ma = magicActions.toArray(new MagicAction[]{});
            action = ma[randomNum(ma.length) - 1];

            if (((MagicAction) action).getStaminaPoints() > scene.getEnemy().getStamina()) {
                action = actions[randomNum(actions.length) - 1];
            }
        } else {
            action = actions[randomNum(actions.length) - 1];
        }

        damage = scene.getEnemy().doAttack(action);
        scene.resetAttackInfos();

        boolean finish = false;
        if (damage == 0) {
            // miss
            scene.setAttackInfo2("Enemy miss!");
        } else {
            scene.setAttackInfo(String.format("Enemy attack %s!", action.getName()));

            int hit = scene.getHero().receiveAttack(action, damage);
            if(hit > 0) {
                // hit
                scene.setAttackInfo2(String.format("You took %d damage!", hit));
            } else {
                // dodge
                scene.setAttackInfo2("You dodged the attack!");
            }

            if (!scene.getHero().isAlive()) {
                finish = true;
                scene.setAttackInfo2(String.format("You took %d damage, you lose!", hit));
                scene.getHero().addExperience(scene.getEnemy().getExpPoints() / 2);
            }
        }

        scene.update();

        return new BattleSceneTurn(true, scene, finish);
    }

    int randomNum(int max) {
       return new Random().nextInt(max) + 1;
    }

}
