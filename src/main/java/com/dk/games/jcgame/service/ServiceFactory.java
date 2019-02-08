package com.dk.games.jcgame.service;

import com.dk.games.jcgame.model.GameConfig;
import com.dk.games.jcgame.service.impl.*;

/**
 * Services Factory
 */
public class ServiceFactory {

    public static RepositoryService getRepositoryService() {
        return new FileRepositoryService();
    }

    public static CharService getCharService(GameConfig config, RepositoryService repositoryService) {
        return new CharServiceImpl(config, repositoryService);
    }

    public static SceneService getSceneService(GameConfig config) {
        return new SceneServiceImpl(config);
    }

    public static AnsiRenderService getAnsiRenderService() {
        return new AnsiRenderService();
    }

    public static PlayerService getPlayerService(GameConfig config, RepositoryService repository) {
        return new PlayerServiceImpl(config, repository);
    }

    public static BattleService getBattleServiceImpl(SceneService sceneService) {
        return new BattleServiceImpl(sceneService);
    }
}
