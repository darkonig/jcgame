package com.dk.games.jcgame.controller;

import com.dk.games.jcgame.constant.GameConstants;
import com.dk.games.jcgame.core.SceneParam;
import com.dk.games.jcgame.core.navigation.NavigationCommand;
import com.dk.games.jcgame.core.navigation.NavigationListener;
import com.dk.games.jcgame.core.navigation.NavigationProcessor;
import com.dk.games.jcgame.core.navigation.impl.NavigationProcessorMap;
import com.dk.games.jcgame.core.navigation.impl.*;
import com.dk.games.jcgame.core.scene.CharacterScene;
import com.dk.games.jcgame.core.scene.Scene;
import com.dk.games.jcgame.core.scene.SceneFact;
import com.dk.games.jcgame.model.Char;
import com.dk.games.jcgame.model.IBattleChar;
import com.dk.games.jcgame.model.Player;
import com.dk.games.jcgame.model.SavePoint;
import com.dk.games.jcgame.service.*;
import com.dk.games.jcgame.service.exception.LoadException;
import com.dk.games.jcgame.service.exception.SaverException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class GamePlay {

    private RenderService renderService;
    private final CharService charService;
    private BattleService battleService;
    private Player player;
    private SceneService sceneService;
    private PlayerService playerService;

    public GamePlay(RenderService renderService
            , SceneService sceneService, PlayerService playerService
            , CharService charService
            , BattleService battleService) {
        this.renderService = renderService;
        this.sceneService = sceneService;
        this.playerService = playerService;
        this.charService = charService;
        this.battleService = battleService;
    }

    public void start() throws LoadException, SaverException {
        renderService.init();
        renderService.render(sceneService.loadScene(null, GameConstants.SCENE_INTRO));
        renderService.freezeScene(500);

        Optional<SavePoint> opSavePoint = playerService.getSavePoint();
        Scene scene = null;
        // load savedScene
        if (opSavePoint.isPresent()) {
            SavePoint savePoint = opSavePoint.get();
            scene = savePoint.getCurrentScene();
            player = savePoint.getPlayer();
        } else {
            // create player
            createPlayer(charService.getChars().stream().filter(Char::isHero).collect(Collectors.toList()));
        }

        if (scene == null){
            scene = sceneService.loadScene(null,GameConstants.SCENE_FIRST);
        }

        // available commands
        processCommands(scene);

        stop();
    }

    private void processCommands(Scene scene) throws LoadException {
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

        // save infos
        SavePoint savePoint = playerService.getSavePoint().orElseGet(SavePoint::new);
        savePoint.setPlayer(player);
        savePoint.setCurrentScene(scene);

        // play
        NavigationProcessor nav = NavigationProcessorMap.builder(sceneService, charService, battleService, player, scene)
                .commands(commands)
                .listener(new NavigationListener() {
                    @Override
                    public void removeFacts(Scene scene) {
                        if (!savePoint.getViewedFacts().isEmpty()) {
                            scene.removeFacts(savePoint.getViewedFacts().stream()
                                    .filter(e -> e.getScene().equals(scene.getName()))
                                    .collect(Collectors.toList()) );
                        }
                    }

                    @Override
                    public void saveViewFact(Scene scene, SceneFact fact) {
                        try {
                            savePoint.setCurrentScene(scene);
                            savePoint.addViewedFacts(fact);
                            playerService.save(savePoint);
                        } catch (SaverException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .build();

        nav = nav.next("");

        while(!nav.isFinished()) {
            renderService.render(nav.getScene());

            if (nav.freezeSceneMilis() > 0) {
                renderService.freezeScene(nav.freezeSceneMilis());
            }

            if (nav.isWaitUserInput()) {
                nav = nav.next(renderService.getUserInput());
            } else {
                nav = nav.next("");
            }
        }
    }

    private void createPlayer(List<IBattleChar> chars) throws LoadException, SaverException {
        Scene scene = sceneService.loadScene(null, GameConstants.SCENE_PLAYER_CREATION);
        CharacterScene charScene = new CharacterScene(scene, chars);
        renderService.render(charScene.getScene());

        String input;
        do {
            input = renderService.getUserInput();
        } while(!charScene.isFinished(input));

        player = Player.create(charScene.getSelectedName(), charScene.getSelectedChar());

        SavePoint savePoint = new SavePoint();
        savePoint.setPlayer(player);
        playerService.save(savePoint);

        // open help
        SceneParam param = new SceneParam();
        param.setPlayer(player);
        new NavigationCommandHelp(sceneService, renderService).execute(param);
    }

    public void stop() {
        renderService.stop();
    }

}
