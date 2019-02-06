package com.dk.games.jcgame.service;

import com.dk.games.jcgame.model.*;
import com.dk.games.jcgame.service.exception.LoadException;
import com.dk.games.jcgame.service.exception.SaverException;
import com.dk.games.jcgame.service.impl.CharServiceImpl;
import com.dk.games.jcgame.service.impl.FileRepositoryService;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CharServiceTest {

    private GameConfig config;
    private RepositoryService repository;
    private CharService service;
    private List<IBattleChar> charList;

    @Before
    public void setUp() {
        config = new GameConfig();
        config.setCharacters("target/charactersTest.data");

        repository = mock(RepositoryService.class);
        service = new CharServiceImpl(config, repository);

        charList = Arrays.asList(
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
    }

    @Test
    public void saveAll() throws SaverException {
        // do
        service.saveAll(charList);

        // assert
        verify(repository, times(1)).saveAll(anyString(), anyCollection());
    }

    @Test
    public void getChars() throws LoadException {
        // when
        ((CharServiceImpl)service).resetCache();
        doReturn(charList).when(repository).getObject(anyString());

        // do
        List<IBattleChar> chars = service.getChars();

        // assert
        assertThat(chars).isNotEmpty();
        verify(repository, times(1)).getObject(anyString());
    }

    @Test
    public void getCharsFromCache() throws LoadException, SaverException {
        // put in cache
        service.saveAll(charList);

        // when
        doReturn(charList).when(repository).getObject(anyString());

        // do
        List<IBattleChar> chars = service.getChars();

        // assert
        assertThat(chars).isNotEmpty();
        verify(repository, times(0)).getObject(anyString());
    }

}