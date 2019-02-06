package com.dk.games.jcgame.service;

import com.dk.games.jcgame.core.scene.BattleScene;
import com.dk.games.jcgame.model.Enemy;
import com.dk.games.jcgame.model.Player;
import com.dk.games.jcgame.service.exception.LoadException;

public interface BattleService {

    BattleSceneTurn begin(Player hero, Enemy enemy) throws LoadException;

    BattleScene chooseAction(BattleScene parent, boolean magic) throws LoadException;

    BattleSceneTurn execPlayerInput(BattleScene scene, String input) throws LoadException;

    BattleSceneTurn execEnemyTurn(BattleScene scene);

}
