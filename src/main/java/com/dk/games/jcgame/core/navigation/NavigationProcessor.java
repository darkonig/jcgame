package com.dk.games.jcgame.core.navigation;

import com.dk.games.jcgame.constant.GameConstants;
import com.dk.games.jcgame.core.SceneParam;
import com.dk.games.jcgame.core.navigation.impl.*;
import com.dk.games.jcgame.core.scene.Scene;
import com.dk.games.jcgame.core.scene.SceneMoveAction;
import com.dk.games.jcgame.model.Enemy;
import com.dk.games.jcgame.model.Player;
import com.dk.games.jcgame.model.SavePoint;
import com.dk.games.jcgame.core.scene.SceneFact;
import com.dk.games.jcgame.service.*;
import com.dk.games.jcgame.service.exception.LoadException;
import com.dk.games.jcgame.service.exception.SaverException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class NavigationProcessor {

    private SceneService sceneService;
    private RenderService renderService;
    private PlayerService playerService;
    private CharService charService;
    private BattleService battleService;
    private Player player;
    private SavePoint savePoint;

    private NavigationProcessor(SceneService sceneService, RenderService renderService
            , PlayerService playerService, CharService charService, BattleService battleService, Player player) {
        this.sceneService = sceneService;
        this.renderService = renderService;
        this.playerService = playerService;
        this.charService = charService;
        this.battleService = battleService;
        this.player = player;
    }

    public void start(Scene scene) throws LoadException {
        savePoint = playerService.getSavePoint().orElseGet(SavePoint::new);
        savePoint.setPlayer(player);
        savePoint.setCurrentScene(scene);

        final String VK_UP = "W";
        final String VK_LEFT = "A";
        final String VK_DOWN = "Z";
        final String VK_RIGHT = "S";

        final String VK_EXIT = "Q";
        final String VK_STATUS = "STATUS";
        final String VK_HELP = "H";

        Map<String, NavigationCommand> commands = new HashMap<>();
        commands.put(VK_UP, new NavigationCommandUp());
        commands.put(VK_DOWN, new NavigationCommandDown());
        commands.put(VK_LEFT, new NavigationCommandLeft());
        commands.put(VK_RIGHT, new NavigationCommandRight());
        commands.put(VK_EXIT, new NavigationCommandExit(playerService));
        commands.put(VK_STATUS, new NavigationCommandStatus(sceneService, renderService));
        commands.put(VK_HELP, new NavigationCommandHelp(sceneService, renderService));

        SceneParam param = new SceneParam();
        param.setScene(scene);
        param.setPlayer(player);

        if (!savePoint.getViewedFacts().isEmpty()) {
            final String sceneName = scene.getName();

            scene.removeFacts(savePoint.getViewedFacts().stream()
                    .filter(e -> e.getScene().equals(sceneName))
                    .collect(Collectors.toList()) );
        }

        renderService.render(scene);

        // check if fact already show up
        while (scene.hasLoadFact()) {
            showFact(scene, scene.popLoadFact());
        }

        //boolean finished = false;
        while (true) {
            String input = renderService.getUserInput();
            if (input == null) {
                //finished = false;
                break;
            }

            input = input.toUpperCase();

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
                //finished = true;
                break;
            }

            // try to make the move
            SceneMoveAction moveAction = scene.move(charService, position);
            if (moveAction != null) {
                if (moveAction.hasSceneFact()) {
                    showFact(scene, moveAction.getSceneFact());
                }

                if (moveAction.hasNextScene()) {
                    // go to the next scene
                    String name = moveAction.getNextScene();
                    scene = sceneService.loadScene(scene.getName(), name);
                    param.setScene(scene);

                    if (!savePoint.getViewedFacts().isEmpty()) {
                        scene.removeFacts(savePoint.getViewedFacts().stream()
                                .filter(e -> e.getScene().equals(name))
                                .collect(Collectors.toList()) );
                    }

                    renderService.render(scene);
                }

                if (moveAction.getEnemy() != null) {
                    // goes to battle mode
                    startBattle(moveAction.getEnemy());
                }
            }

            // check if fact already show up
            while (scene.hasLoadFact()) {
                showFact(scene, scene.popLoadFact());
            }

            renderService.render(scene);
        }
    }

    private void showFact(Scene scene, SceneFact fact) throws LoadException {
        Scene infoScene;
        if (fact.isFullInfo()) {
            infoScene = sceneService.loadScene(scene.getName(), GameConstants.SCENE_FACT_FULL_TEXT);
        } else {
            infoScene = sceneService.loadScene(scene.getName(), GameConstants.SCENE_FACT_TEXT);
        }

        // render a fact
        scene.showFact(player
                , infoScene
                , fact
                , renderService);

        // save current fact showed
        try {
            savePoint.setCurrentScene(scene);
            savePoint.addViewedFacts(fact);
            playerService.save(savePoint);
        } catch (SaverException e) {
            e.printStackTrace();
        }
    }

    private void startBattle(Enemy enemy) throws LoadException {
        BattleSceneTurn turn = battleService.begin(player, enemy);

        renderService.render(turn.getScene());

        while(!turn.isFinished()) {
            while (turn.isPlayerTurn()) {
                String input = renderService.getUserInput();

                turn = battleService.execPlayerInput(turn.getBattleScene(), input);
                renderService.render(turn.getScene());
            }

            renderService.freezeScene(1000);

            if (turn.isFinished()) {
                break;
            }

            turn = battleService.execEnemyTurn(turn.getBattleScene());
            renderService.render(turn.getScene());

            if (turn.isFinished()) {
                renderService.freezeScene(1000);
            }
        }

        player.fireAfterBattle();
    }

    public static NavigationProcessor create(SceneService sceneService, RenderService renderService
            , PlayerService playerService, CharService charService, BattleService battleService, Player player) {
        return new NavigationProcessor(sceneService, renderService, playerService, charService, battleService, player);
    }
}
