package com.dk.games.jcgame.core.navigation.impl;

import com.dk.games.jcgame.core.navigation.NavigationCommand;
import com.dk.games.jcgame.core.navigation.NavigationProcessor;
import com.dk.games.jcgame.core.scene.Scene;
import com.dk.games.jcgame.model.BattleChar;
import com.dk.games.jcgame.model.IBattleChar;
import com.dk.games.jcgame.model.Player;
import com.dk.games.jcgame.model.Skill;
import com.dk.games.jcgame.model.action.ActionCrazyHowl;
import com.dk.games.jcgame.model.action.ActionStrongBite;
import com.dk.games.jcgame.service.BattleService;
import com.dk.games.jcgame.service.CharService;
import com.dk.games.jcgame.service.PlayerService;
import com.dk.games.jcgame.service.SceneService;
import com.dk.games.jcgame.service.exception.LoadException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NavigationProcessorMapTest {

    private NavigationProcessor processor;
    private SceneService sceneService;
    private CharService charService;
    private BattleService battleService;
    private Player player;
    private Scene scene;
    private PlayerService playerService;

    @Before
    public void setup() {
        IBattleChar heroChar = BattleChar.builder()
                .name("Human")
                .addSkill(Skill.STRENGTH, 2)
                .addSkill(Skill.ABILITY, 2)
                .addSkill(Skill.INTELLIGENCE, 3)
                .addSkill(Skill.RESISTANCE, 1)
                .addAction(new ActionStrongBite())
                .addMagicAction(new ActionCrazyHowl())
                .hero(true)
                .build();

        player = spy(Player.create("Hero", heroChar));

        scene = new Scene("test-scene", null);
        scene.setBaseScene("%hero    %");

        sceneService = mock(SceneService.class);
        charService = mock(CharService.class);
        battleService = mock(BattleService.class);
        playerService = mock(PlayerService.class);

        final String VK_UP = "W";
        final String VK_LEFT = "A";
        final String VK_DOWN = "Z";
        final String VK_RIGHT = "S";

        final String VK_EXIT = "Q";

        Map<String, NavigationCommand> commands = new HashMap<>();
        commands.put(VK_UP, new NavigationCommandUp());
        commands.put(VK_DOWN, new NavigationCommandDown());
        commands.put(VK_LEFT, new NavigationCommandLeft());
        commands.put(VK_RIGHT, new NavigationCommandRight());
        commands.put(VK_EXIT, new NavigationCommandExit(playerService));

        processor = NavigationProcessorMap.builder(sceneService, charService
                , battleService, player, scene)
                .commands(commands)
                .build();
    }

    @Test
    public void next_exit() throws LoadException {
        // when
        doReturn(Optional.empty()).when(playerService).getSavePoint();

        // do
        NavigationProcessor next = processor.next("Q");

        // assert
        assertThat(next.isFinished()).isTrue();
    }

    @Test(expected = RuntimeException.class)
    public void next_throwCommandsError() throws LoadException {
        ((NavigationProcessorMap)processor).setCommands(null);
        processor.next("");
    }

    @Test
    public void next_moveRight() throws LoadException {
        // when
        doReturn(Optional.empty()).when(playerService).getSavePoint();
        scene.setBaseScene(" s@  ");

        // do
        NavigationProcessor next = processor.next("S");

        // assert
        assertThat(next.getScene().getScene()).isEqualTo("  @ ");
        assertThat(next.getScene().getPosition()).isEqualTo(2);
    }

    @Test
    public void next_moveRight2paces() throws LoadException {
        // when
        doReturn(Optional.empty()).when(playerService).getSavePoint();
        scene.setBaseScene(" s@  ");

        // do
        NavigationProcessor next = processor.next("S2");

        // assert
        assertThat(next.getScene().getScene()).isEqualTo("   @");
        assertThat(next.getScene().getPosition()).isEqualTo(3);
    }

    @Test
    public void next_moveNextScene() throws LoadException {
        Scene firstScene = new Scene("first", "test-scene");
        firstScene.setBaseScene("11@ \n" +
                "!---------------\n" +
                "$link:1=test-scene:1@\n");

        //when
        doReturn(Optional.empty()).when(playerService).getSavePoint();
        doReturn(firstScene).when(sceneService).loadScene(anyString(), eq("first"));
        this.scene.setBaseScene(" s@1@1\n" +
                "!---------------\n" +
                "$link:1=first:1@\n");

        assertThat(this.scene.getScene()).isEqualTo(" @  1\n");

        // do
        NavigationProcessor next = processor.next("S3");

        // assert
        assertThat(next.getScene().getScene()).isEqualTo("1@  \n");
        assertThat(next.getScene().getPosition()).isEqualTo(1);
    }

    @Test
    public void next_sceneFact() throws LoadException {
        Scene firstScene = new Scene("info-text", null);
        firstScene.setBaseScene(
        " __________________________________________________\n" +
        "| %name                                          % |\n" +
        "| %text                                          % |\n" +
        "| %text                                          % |\n" +
        "| %text                                        % > |\n" +
        " --------------------------------------------------");

        //when
        doReturn(Optional.empty()).when(playerService).getSavePoint();
        doReturn(firstScene).when(sceneService).loadScene(anyString(), eq("info-text"));
        this.scene.setBaseScene(
                "................................####################\n" +
                "..------------..................#   -------------2@2\n" +
                ".|  Fifth Av. |.................#  |  ##############\n" +
                "..------------..................#  |  #.............\n" +
                "................................#  |  #.............\n" +
                "................................#  |  #.............\n" +
                "................................#  |  #.............\n" +
                "................................#  |  #.............\n" +
                "................................#  |  #.............\n" +
                "................................#  |  #.............\n" +
                "................................#  |  #.............\n" +
                ".........................########  |  #.............\n" +
                ".........................#   ------   #.............\n" +
                ".........................#  |  ########.............\n" +
                ".........................#  |  #....................\n" +
                "............##############  |s@ #....................\n" +
                "............# i1            |  #....................\n" +
                "............##############  |1@#....................\n" +
                ".........................#11111#....................\n" +

                "\n!---------------\n" +
                "$fact:1:load=[\n" +
                "    Mission: You found the place!  Now go find the trash cans, you can usually find it at the back of the building.\n" +
                "]\n");

        // do
        NavigationProcessor next = processor.next("");

        // assert
        assertThat(next).isInstanceOf(NavigationProcessorSceneFact.class);
        assertThat(next.getScene().getScene()).contains("You found the place!");

        NavigationProcessor next1 = next.next(">");

        assertThat(next1).isInstanceOf(NavigationProcessorMap.class);
        assertThat(next1.getScene().getScene()).doesNotContain("You found the place!");
    }
}