package com.dk.games.jcgame.controller;

import com.dk.games.jcgame.constant.GameConstants;
import com.dk.games.jcgame.core.SceneParam;
import com.dk.games.jcgame.core.navigation.NavigationProcessor;
import com.dk.games.jcgame.core.navigation.impl.NavigationCommandHelp;
import com.dk.games.jcgame.core.scene.CharacterScene;
import com.dk.games.jcgame.core.scene.Scene;
import com.dk.games.jcgame.model.Char;
import com.dk.games.jcgame.model.IBattleChar;
import com.dk.games.jcgame.model.Player;
import com.dk.games.jcgame.model.SavePoint;
import com.dk.games.jcgame.service.*;
import com.dk.games.jcgame.service.exception.LoadException;
import com.dk.games.jcgame.service.exception.SaverException;

import java.util.List;
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

        // play
        NavigationProcessor
                .create(sceneService, renderService, playerService, charService, battleService, player)
                .start(scene);

        stop();
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
