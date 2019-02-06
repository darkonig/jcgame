package com.dk.games.jcgame.core.scene;

import com.dk.games.jcgame.model.Enemy;
import com.dk.games.jcgame.model.Player;
import com.dk.games.jcgame.model.action.Action;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BattleScene {
    private static final String ACTION_MENU_NAME = "ac_menu_name";

    private static final String ATTACK_INFO = "attack_info1";
    private static final String ATTACK_INFO_2 = "attack_info2";

    private final Scene scene;
    private final Player hero;
    private final Enemy enemy;

    private BattleScene parent;
    private boolean actions;
    private boolean magicActions;

    private Map<String, Action> availableActions;
    private String attackInfo;
    private String attackInfo2;

    public BattleScene(Scene scene, Player hero, Enemy enemy) {
        this(scene, hero, enemy, false, false);
    }

    public BattleScene(Scene scene, Player hero, Enemy enemy, boolean actions, boolean magicActions) {
        this.scene = scene;
        this.hero = hero;
        this.enemy = enemy;
        this.actions = actions;
        this.magicActions = magicActions;
        availableActions = new HashMap<>();
        processTexts();
    }

    public BattleScene(BattleScene parent, Scene scene, boolean actions, boolean magicActions) {
        this(scene, parent.getHero(), parent.getEnemy(), actions, magicActions);
        this.parent = parent;
    }

    public boolean isActions() {
        return actions;
    }

    public boolean isMagicActions() {
        return magicActions;
    }

    public Scene getScene() {
        return scene;
    }

    public Player getHero() {
        return hero;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public BattleScene getParent() {
        return parent;
    }

    public void setAttackInfo(String attackInfo) {
        this.attackInfo = attackInfo;
    }

    public String getAttackInfo() {
        return attackInfo;
    }

    public String getAttackInfo2() {
        return attackInfo2;
    }

    public void setAttackInfo2(String attackInfo2) {
        this.attackInfo2 = attackInfo2;
    }

    public void resetAttackInfos() {
        attackInfo = null;
        attackInfo2 = null;
    }

    public void update() {
        processTexts();
    }

    private void processTexts() {
        AtomicInteger actionIndex = new AtomicInteger(0);
        final Iterator<? extends Action> actions;
        if (isActions())
            actions = hero.getActions().iterator();
        else if (isMagicActions())
            actions = hero.getMagicActions().iterator();
        else
            actions = null;

        BaseSceneActions s = new BaseSceneActions();
        s.addProcessor(hero, false, () -> {
            if (actions != null && actions.hasNext()) {
                Action action = actions.next();
                availableActions.put(String.valueOf(actionIndex.incrementAndGet()), action);
                return String.format("%d. %s", actionIndex.get(), action.toString());
            }

            return "";
        });
        s.addProcessor(enemy, true);

        s.getProcessor().addProcessor(ACTION_MENU_NAME, () -> isActions() ? "PHYSICAL ATTACKS" : "MAGICS ATTACKS");
        if (attackInfo != null) {
            s.getProcessor().addProcessor(ATTACK_INFO, () -> attackInfo);
        }
        if (attackInfo2 != null) {
            s.getProcessor().addProcessor(ATTACK_INFO_2, () -> attackInfo2);
        }

        scene.setScene(s.processTexts(scene));
    }

    public BattleSceneAction setPlayerInput(String input) {
        if (isActions() || isMagicActions()) {
            if ("<".equals(input)) {
                return new BattleSceneAction(BattleSceneAction.TYPE_SCENE_BEGIN);
            }

            Action action = availableActions.get(input);
            if (action != null) {
                return new BattleSceneAction(action);
            }
            return null;
        }

        if ("1".equals(input)) {
            return new BattleSceneAction(BattleSceneAction.TYPE_SCENE_ACTIONS);
        }
        if ("2".equals(input)) {
            return new BattleSceneAction(BattleSceneAction.TYPE_SCENE_MAGICAL_ACTIONS);
        }
        if ("3".equals(input)) {
            return new BattleSceneAction(BattleSceneAction.TYPE_SCENE_FLEE);
        }

        return null;
    }

    public class BattleSceneAction {
        public static final int TYPE_ACTION = 1;
        public static final int TYPE_SCENE_ACTIONS = 2;
        public static final int TYPE_SCENE_MAGICAL_ACTIONS = 3;
        public static final int TYPE_SCENE_FLEE = 4;
        public static final int TYPE_SCENE_BEGIN = 5;

        private int actionType;
        private Action action;

        BattleSceneAction(Action action) {
            this.actionType = TYPE_ACTION;
            this.action = action;
        }

        BattleSceneAction(int actionType) {
            this.actionType = actionType;
        }

        public int getActionType() {
            return actionType;
        }

        public Action getAction() {
            return action;
        }
    }
}
