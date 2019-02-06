package com.dk.games.jcgame.service;

import com.dk.games.jcgame.model.*;
import com.dk.games.jcgame.model.action.ActionFire;
import com.dk.games.jcgame.model.action.ActionStrongPunch;
import com.dk.games.jcgame.service.exception.SaverException;
import com.dk.games.jcgame.service.impl.PlayerServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class PlayerServiceTest {

    private GameConfig config;
    private PlayerService service;
    private Player player;
    private RepositoryService repository;

    @Before
    public void setUp() {
        config = new GameConfig();
        config.setSavedData("target/save_test.data");

        repository = mock(RepositoryService.class);

        service = new PlayerServiceImpl(config, repository);

        player = Player.create("Hero", BattleChar.builder()
                .name("Human")
                .addSkill(Skill.STRENGTH, 2)
                .addSkill(Skill.ABILITY, 2)
                .addSkill(Skill.INTELLIGENCE, 3)
                .addSkill(Skill.RESISTANCE, 1)
                .addAction(new ActionStrongPunch())
                .addMagicAction(new ActionFire())
                .build());
    }

    @Test
    public void save() throws SaverException {
        SavePoint savePoint = new SavePoint();
        savePoint.setCurrentScene(null);
        savePoint.setPlayer(player);

        SavePoint saved = service.save(savePoint);

        assertThat(saved.getDate()).isNotNull();
    }

    @Test
    public void getSavePoint() {
        SavePoint savePoint = new SavePoint();
        savePoint.setCurrentScene(null);
        savePoint.setPlayer(player);

        doReturn(Optional.of(savePoint)).when(repository).tryGetObject(anyString());

        Optional<SavePoint> saved = service.getSavePoint();

        assertThat(saved.isPresent()).isTrue();
        assertThat(saved.get().getPlayer().getName()).isEqualTo(player.getName());
    }
}