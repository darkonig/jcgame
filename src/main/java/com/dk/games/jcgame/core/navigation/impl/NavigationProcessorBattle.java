package com.dk.games.jcgame.core.navigation.impl;

import com.dk.games.jcgame.core.navigation.NavigationProcessor;
import com.dk.games.jcgame.core.scene.Scene;
import com.dk.games.jcgame.model.Enemy;
import com.dk.games.jcgame.model.Player;
import com.dk.games.jcgame.service.BattleSceneTurn;
import com.dk.games.jcgame.service.BattleService;
import com.dk.games.jcgame.service.exception.LoadException;

public class NavigationProcessorBattle implements NavigationProcessor {

    private NavigationProcessor parent;
    private BattleSceneTurn turn;
    private BattleService battleService;
    private Player player;
    private boolean waitUserInput = true;
    private long freezeTime;

    public NavigationProcessorBattle(NavigationProcessor parent
            , BattleService battleService, Player player, Enemy enemy) throws LoadException {
        this.parent = parent;
        this.battleService = battleService;
        this.player = player;

        turn = battleService.begin(player, enemy);
    }

    @Override
    public NavigationProcessor next(String input) throws LoadException {
        if (turn.isFinished()) {
            return parent;
        }

        freezeTime = 0;
        waitUserInput = true;
        if (turn.isPlayerTurn()) {
            turn = battleService.execPlayerInput(turn.getBattleScene(), input);
        } else {
            turn = battleService.execEnemyTurn(turn.getBattleScene());
        }

        if (!turn.isPlayerTurn()) {
            waitUserInput = false;
            freezeTime = 1000;
        }

        if (turn.isFinished()) {
            player.fireAfterBattle();
            return this;
        }

        return this;
    }

    @Override
    public Scene getScene() {
        return turn.getScene();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean isWaitUserInput() {
        return waitUserInput;
    }

    @Override
    public long freezeSceneMilis() {
        return freezeTime;
    }

}
