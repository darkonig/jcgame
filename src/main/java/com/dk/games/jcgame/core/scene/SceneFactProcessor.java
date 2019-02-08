package com.dk.games.jcgame.core.scene;

import com.dk.games.jcgame.model.Player;
import com.dk.games.jcgame.utils.StringUtils;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SceneFactProcessor {

    private String scene;
    private Iterator<SceneFact.FactMessage> iterator;
    private Player player;
    private Scene infoScene;

    private String currentMsg;
    private SceneFact.FactMessage msg;
    private String currentName;

    public SceneFactProcessor(Player player, Scene scene, Scene infoScene, SceneFact fact) {
        this.player = player;
        this.infoScene = infoScene;

        int lines = (int) infoScene.getScene().chars().filter(e -> e == '\n').count();

        // get a piece of the current scene
        this.scene = scene.getScene().substring(0, (scene.getScene().length() - lines * scene.getRowLength()) - 1);
        this.iterator = fact.getMessages().iterator();
    }

    public String getSceneNext() {
        // iterates through all the fact messages
        if (isFinished()) {
            return null;
        }

        if (currentMsg == null || currentMsg.length() == 0) {
            msg = iterator.next();
            currentMsg = msg.getMsg();
            currentMsg = currentMsg.replaceAll("\\$hero", player.getName());
            currentName = null;
        }

        // while there is text on the message, print it
        // Set message name
        String textScene;

        if (currentName == null) {
            textScene = infoScene.getBaseScene();
            String name;
            if ("$hero".equals(msg.getName())) {
                name = player.getName();
            } else {
                name = msg.getName();
            }

            Pattern compile = Pattern.compile("%name.*%");
            Matcher matcher = compile.matcher(textScene);
            if (matcher.find()) {
                String g = matcher.group();

                if (name.length() < g.length()) {
                    name = StringUtils.padRight(name, g.length());
                }
                textScene = textScene.replaceFirst(g, name);
            }

            currentName = textScene;
        } else {
            textScene = currentName;
        }

        // fits the fact message into the scene
        Pattern compile = Pattern.compile("%text.*%");
        Matcher matcher = compile.matcher(textScene);

        while (matcher.find()) {
            String g = matcher.group();

            String replace = "";

            // reducing fact message
            if (currentMsg.length() > g.length()) {
                replace = currentMsg.substring(0, g.length());
                currentMsg = currentMsg.substring(g.length());
            } else if (currentMsg.length() > 0) {
                replace = currentMsg;
                currentMsg = "";
            }

            if (replace.length() < g.length()) {
                replace = StringUtils.padRight(replace, g.length());
            }

            textScene = textScene.replaceFirst(g, replace);
        }

        // print scene
        return scene + textScene;
    }

    public boolean isFinished() {
        return !iterator.hasNext();
    }

}
