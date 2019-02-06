package com.dk.games.jcgame.service;

import com.dk.games.jcgame.model.*;
import com.dk.games.jcgame.service.exception.LoadException;
import com.dk.games.jcgame.service.exception.SaverException;
import com.dk.games.jcgame.service.impl.FileRepositoryService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryServiceTest {

    private RepositoryService service;
    private String saveTarget = "target/saveTest.data";

    @Before
    public void setUp() {
        service = spy(new FileRepositoryService());
    }

    @After
    public void tearDown() throws Exception {
        Files.deleteIfExists(Paths.get(saveTarget));
    }

    @Test
    public void tryGetObject_success() throws SaverException {
        save_success();

        Optional<SavePoint> result = service.tryGetObject(saveTarget);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getPlayer().getName()).isEqualTo("Frodo");
    }

    @Test
    public void tryGetObject_empty() {
        Optional<SavePoint> player = service.tryGetObject(saveTarget);

        assertThat(player.isPresent()).isFalse();
    }


    @Test
    public void getObject_success() throws SaverException, LoadException {
        save_success();

        SavePoint result = service.getObject(saveTarget);

        assertThat(result).isNotNull();
        assertThat(result.getPlayer().getName()).isEqualTo("Frodo");
    }

    @Test(expected = LoadException.class)
    public void getObject_error() throws Exception {
        service.getObject(saveTarget);
    }

    @Test
    public void save_success() throws SaverException {
        Player player = Player.create("Frodo"
                , BattleChar.builder().name("Human")
                        .addSkill(Skill.STRENGTH, 2)
                        .addSkill(Skill.INTELLIGENCE, 3)
                        .addSkill(Skill.RESISTANCE, 1)
                        .build());

        SavePoint t = new SavePoint();
        t.setPlayer(player);

        service.save(saveTarget, t);

        assertThat(Files.exists(Paths.get(saveTarget))).isTrue();
    }

    @Test(expected = SaverException.class)
    public void save_error() throws SaverException, IOException {
        Player player = Player.create("Frodo"
                , BattleChar.builder().name("Human")
                        .addSkill(Skill.STRENGTH, 2)
                        .addSkill(Skill.INTELLIGENCE, 3)
                        .addSkill(Skill.RESISTANCE, 1)
                        .build());

        SavePoint t = new SavePoint();
        t.setPlayer(player);

        doThrow(new IOException()).when(((FileRepositoryService)service)).saveObject(anyString(), anyObject());

        service.save(saveTarget, t);
    }

    @Test
    public void saveAll_success() throws SaverException {
        List<Char> saveChars = Arrays.asList(
                BattleChar.builder()
                        .name("Human")
                        .addSkill(Skill.STRENGTH, 2)
                        .addSkill(Skill.ABILITY, 2)
                        .addSkill(Skill.INTELLIGENCE, 3)
                        .addSkill(Skill.RESISTANCE, 1).build()
                , BattleChar.builder()
                        .name("Elf")
                        .addSkill(Skill.STRENGTH, 2)
                        .addSkill(Skill.ABILITY, 2)
                        .addSkill(Skill.INTELLIGENCE, 3)
                        .addSkill(Skill.RESISTANCE, 1).build()
        );
        service.saveAll(saveTarget, saveChars);

        assertThat(Files.exists(Paths.get(saveTarget))).isTrue();
    }

    @Test(expected = SaverException.class)
    public void saveAll_error() throws Exception {
        List<Char> saveChars = Arrays.asList(
                BattleChar.builder()
                        .name("Human")
                        .addSkill(Skill.STRENGTH, 2)
                        .addSkill(Skill.ABILITY, 2)
                        .addSkill(Skill.INTELLIGENCE, 3)
                        .addSkill(Skill.RESISTANCE, 1).build()
                , BattleChar.builder()
                        .name("Elf")
                        .addSkill(Skill.STRENGTH, 2)
                        .addSkill(Skill.ABILITY, 2)
                        .addSkill(Skill.INTELLIGENCE, 3)
                        .addSkill(Skill.RESISTANCE, 1).build()
        );

        doThrow(new IOException()).when(((FileRepositoryService)service)).saveObject(anyString(), anyObject());

        service.saveAll(saveTarget, saveChars);
    }
}