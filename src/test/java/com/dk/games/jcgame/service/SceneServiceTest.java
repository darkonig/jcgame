package com.dk.games.jcgame.service;

import com.dk.games.jcgame.model.GameConfig;
import com.dk.games.jcgame.core.scene.Scene;
import com.dk.games.jcgame.service.exception.LoadException;
import com.dk.games.jcgame.service.impl.SceneServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;

public class SceneServiceTest {

    private SceneService service;
    private GameConfig config;

    @Before
    public void setUp() {
        config = new GameConfig();
        service = spy(new SceneServiceImpl(config));
    }

    @Test
    public void loadScene() throws LoadException {
        // do
        Scene scene = service.loadScene(null, "first", "first.scene");

        // assert
        assertThat(scene).isNotNull();
        assertThat(scene.getBaseScene()).isNotEmpty();
    }

    @Test
    public void loadScene2() throws LoadException {
        // do
        Scene scene = service.loadScene(null, "first");

        // assert
        assertThat(scene).isNotNull();
        assertThat(scene.getBaseScene()).isNotEmpty();
    }

    @Test(expected = LoadException.class)
    public void loadScene_errorNoResource() throws LoadException {
        // do
        service.loadScene(null, "first", "first1.scene");
    }

    @Test(expected = LoadException.class)
    public void loadScene_throwIO() throws Exception {
        doThrow(new IOException()).when((SceneServiceImpl)service).readFile(anyString());

        // do
        service.loadScene(null, "first", "first.scene");
    }
}