package com.dk.games.jcgame;

import com.dk.games.jcgame.controller.GamePlay;
import com.dk.games.jcgame.model.*;
import com.dk.games.jcgame.model.action.*;
import com.dk.games.jcgame.service.*;
import com.dk.games.jcgame.service.exception.SaverException;
import com.dk.games.jcgame.service.exception.LoadException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static void main(String[] args) throws LoadException, SaverException {
        GameConfig config = new GameConfig();
        config.setIntroScene("intro.scene");
        config.setSavedData("save.data");
        config.setCharacters("characters.data");

        RepositoryService repository = ServiceFactory.getRepositoryService();
        CharService charService = ServiceFactory.getCharService(config, repository);
        SceneService sceneService = ServiceFactory.getSceneService(config);

        if (!Files.exists(Paths.get(config.getCharacters()))) {
            repository.saveAll(config.getCharacters(), getChars());
        }

        GamePlay main = new GamePlay(ServiceFactory.getAnsiRenderService()
                , sceneService
                , ServiceFactory.getPlayerService(config, repository)
                , charService
                , ServiceFactory.getBattleServiceImpl(sceneService));
        main.start();
    }

    /**
     * Create chars
     * @return chars
     */
    public static List<BattleChar> getChars() {
        return Arrays.asList(
            BattleChar.builder()
                    .name("York Shire")
                    .hero(true)
                    .addSkill(Skill.STRENGTH, 2)
                    .addSkill(Skill.ABILITY, 3)
                    .addSkill(Skill.INTELLIGENCE, 4)
                    .addSkill(Skill.RESISTANCE, 1)
                    .addAction(new ActionStrongBite())
                    .addAction(new ActionBite())
                    .addAction(new ActionTailAttack())
                    .addMagicAction(new ActionCrazyHowl())
                    .build()
            , BattleChar.builder()
                    .name("Poodle")
                    .hero(true)
                    .addSkill(Skill.STRENGTH, 3)
                    .addSkill(Skill.ABILITY, 2)
                    .addSkill(Skill.INTELLIGENCE, 3)
                    .addSkill(Skill.RESISTANCE, 2)
                    .addAction(new ActionStrongBite())
                    .addAction(new ActionBite())
                    .addAction(new ActionTailAttack())
                    .addMagicAction(new ActionCrazyHowl())
                    .build()
            , BattleChar.builder()
                    .name("Fila")
                    .hero(true)
                    .addSkill(Skill.STRENGTH, 4)
                    .addSkill(Skill.ABILITY, 2)
                    .addSkill(Skill.INTELLIGENCE, 1)
                    .addSkill(Skill.RESISTANCE, 3)
                    .addAction(new ActionStrongBite())
                    .addAction(new ActionBite())
                    .addAction(new ActionTailAttack())
                    .addMagicAction(new ActionCrazyHowl())
                    .build()
            , BattleChar.builder()
                    .name("Bulldog")
                    .addSkill(Skill.STRENGTH, 4)
                    .addSkill(Skill.ABILITY, 3)
                    .addSkill(Skill.INTELLIGENCE, 2)
                    .addSkill(Skill.RESISTANCE, 1)
                    .addAction(new ActionStrongBite())
                    .addAction(new ActionBite())
                    .addMagicAction(new ActionCrazyHowl())
                    .build()
            , BattleChar.builder()
                    .name("Rat")
                    .addSkill(Skill.STRENGTH, 1)
                    .addSkill(Skill.ABILITY, 3)
                    .addSkill(Skill.INTELLIGENCE, 2)
                    .addSkill(Skill.RESISTANCE, 1)
                    .addAction(new ActionBite())
                    .addMagicAction(new ActionCrazyGnaw())
                    .build()
            , BattleChar.builder()
                    .name("Cockroach")
                    .addSkill(Skill.STRENGTH, 1)
                    .addSkill(Skill.ABILITY, 1)
                    .addSkill(Skill.INTELLIGENCE, 1)
                    .addSkill(Skill.RESISTANCE, 1)
                    .addAction(new ActionBite())
                    .addMagicAction(new ActionCrazyGnaw())
                    .build()
            , BattleChar.builder()
                    .name("Abyssinian Cat")
                    .addSkill(Skill.STRENGTH, 2)
                    .addSkill(Skill.ABILITY, 3)
                    .addSkill(Skill.INTELLIGENCE, 3)
                    .addSkill(Skill.RESISTANCE, 1)
                    .addAction(new ActionStrongBite())
                    .addMagicAction(new ActionCrazyHowl())
                    .level(1)
                    .build()
            , BattleChar.builder()
                    .name("Bobtail Cat")
                    .addSkill(Skill.STRENGTH, 4)
                    .addSkill(Skill.ABILITY, 2)
                    .addSkill(Skill.INTELLIGENCE, 1)
                    .addSkill(Skill.RESISTANCE, 3)
                    .addAction(new ActionStrongBite())
                    .addAction(new ActionKick())
                    .addMagicAction(new ActionSuperScratch())
                    .level(1)
                    .build()
            , BattleChar.builder()
                    .name("Police Office")
                    .addSkill(Skill.STRENGTH, 6)
                    .addSkill(Skill.ABILITY, 2)
                    .addSkill(Skill.INTELLIGENCE, 5)
                    .addSkill(Skill.RESISTANCE, 4)
                    .addAction(new ActionKick())
                    .addAction(new ActionPunch())
                    .level(10)
                    .build()
            , BattleChar.builder()
                    .name("Akita")
                    .addSkill(Skill.STRENGTH, 8)
                    .addSkill(Skill.ABILITY, 3)
                    .addSkill(Skill.INTELLIGENCE, 3)
                    .addSkill(Skill.RESISTANCE, 3)
                    .addAction(new ActionStrongBite())
                    .addAction(new ActionBite())
                    .addMagicAction(new ActionCrazyHowl())
                    .level(4)
                    .build()
            , BattleChar.builder()
                    .name("American Foxhound")
                    .addSkill(Skill.STRENGTH, 5)
                    .addSkill(Skill.ABILITY, 2)
                    .addSkill(Skill.INTELLIGENCE, 1)
                    .addSkill(Skill.RESISTANCE, 2)
                    .addAction(new ActionStrongBite())
                    .addAction(new ActionBite())
                    .addMagicAction(new ActionCrazyHowl())
                    .level(3)
                    .build()
            , BattleChar.builder()
                    .name("A. Water Spaniel")
                    .addSkill(Skill.STRENGTH, 4)
                    .addSkill(Skill.ABILITY, 2)
                    .addSkill(Skill.INTELLIGENCE, 2)
                    .addSkill(Skill.RESISTANCE, 2)
                    .addAction(new ActionStrongBite())
                    .addAction(new ActionBite())
                    .addMagicAction(new ActionCrazyHowl())
                    .level(2)
                    .build()
            , BattleChar.builder()
                    .name("A. Staffordshire")
                    .addSkill(Skill.STRENGTH, 8)
                    .addSkill(Skill.ABILITY, 3)
                    .addSkill(Skill.INTELLIGENCE, 3)
                    .addSkill(Skill.RESISTANCE, 5)
                    .addAction(new ActionStrongBite())
                    .addAction(new ActionBite())
                    .addMagicAction(new ActionCrazyHowl())
                    .level(5)
                    .build()
        );
    }

}
