package com.dk.games.jcgame.core.navigation.impl;

import com.dk.games.jcgame.core.SceneParam;
import com.dk.games.jcgame.core.navigation.NavigationCommand;
import com.dk.games.jcgame.core.navigation.NavigationListener;
import com.dk.games.jcgame.core.navigation.NavigationProcessor;
import com.dk.games.jcgame.core.scene.*;
import com.dk.games.jcgame.model.Player;
import com.dk.games.jcgame.service.*;
import com.dk.games.jcgame.service.exception.LoadException;

import java.util.Map;

public class NavigationProcessorMap implements NavigationProcessor {

    private SceneService sceneService;
    private CharService charService;
    private BattleService battleService;
    private Player player;
    private Map<String, NavigationCommand> commands;
    private NavigationListener listener;
    private boolean finished;
    private Scene scene;

    private NavigationProcessorMap(SceneService sceneService
            , CharService charService, BattleService battleService, Player player, Scene scene) {
        this.sceneService = sceneService;
        this.charService = charService;
        this.battleService = battleService;
        this.player = player;
        this.scene = scene;
    }

    public NavigationListener getListener() {
        return listener;
    }

    public void setListener(NavigationListener listener) {
        this.listener = listener;
    }

    public Map<String, NavigationCommand> getCommands() {
        return commands;
    }

    public void setCommands(Map<String, NavigationCommand> commands) {
        this.commands = commands;
    }

    @Override
    public NavigationProcessor next(String input) throws LoadException {
        if (commands == null) {
            throw new RuntimeException("Commands can not be null.");
        }

        input = input.toUpperCase();

        SceneParam param = new SceneParam();
        param.setScene(scene);
        param.setPlayer(player);

        // get number of paces
        int paces = 1;
        if (input.matches("[A-Z]\\d+")) {
            paces = Integer.valueOf(input.substring(1));
            input = input.substring(0,1);
        }

        param.setPaces(paces);

        int position = scene.getPosition();

        NavigationCommand navigationCommand = commands.get(input);
        if (navigationCommand != null) {
            position = navigationCommand.execute(param);
        }

        if (navigationCommand instanceof NavigationCommandExit) {
            finished = true;
            return this;
        }

        // try to make the move
        SceneMoveAction moveAction = scene.move(charService, position);
        if (moveAction != null) {
            if (moveAction.hasSceneFact()) {
                return new NavigationProcessorSceneFact(this, sceneService, player, moveAction.getSceneFact(), listener);
            }

            if (moveAction.hasNextScene()) {
                // go to the next scene
                String name = moveAction.getNextScene();
                scene = sceneService.loadScene(scene.getName(), name);
                param.setScene(scene);

                if (listener != null) {
                    listener.removeFacts(scene);
                }
            }

            if (moveAction.getEnemy() != null) {
                // goes to battle mode
                return new NavigationProcessorBattle(this, battleService, player, moveAction.getEnemy());
            }
        }

        // check if fact already show up
        if (scene.hasLoadFact()) {
            return new NavigationProcessorSceneFact(this, sceneService, player, scene.popLoadFact(), listener);
        }

        return this;
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public boolean isWaitUserInput() {
        return true;
    }

    @Override
    public long freezeSceneMilis() {
        return 0;
    }

    public static NavigationProcessorBuilder builder(SceneService sceneService
            , CharService charService, BattleService battleService, Player player, Scene scene) {
        return new NavigationProcessorBuilder(new NavigationProcessorMap(sceneService, charService, battleService, player, scene));
    }

    public static class NavigationProcessorBuilder {
        private NavigationProcessorMap processor;

        public NavigationProcessorBuilder(NavigationProcessorMap processor) {
            this.processor = processor;
        }

        public NavigationProcessorBuilder listener(NavigationListener listener) {
            processor.setListener(listener);
            return this;
        }

        public NavigationProcessorBuilder commands(Map<String, NavigationCommand> commands) {
            processor.setCommands(commands);
            return this;
        }

        public NavigationProcessorMap build() {
            return processor;
        }

    }
}
