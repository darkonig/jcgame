package com.dk.games.jcgame.core.scene;

import com.dk.games.jcgame.model.IBattleChar;
import com.dk.games.jcgame.model.Skill;
import com.dk.games.jcgame.model.action.Action;
import com.dk.games.jcgame.utils.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CharacterScene {

    private static final String HERO_NAME = "name";
    private static final String HERO_STRENGTH = "str";
    private static final String HERO_ABILITY = "abl";
    private static final String HERO_RESISTANCE = "rst";
    private static final String HERO_INTELLIGENCE = "int";

    private static final String ACTION = "action";

    private final Scene scene;

    private final List<IBattleChar> characters;

    private IBattleChar selectedChar;
    private String selectedName;

    public CharacterScene(Scene scene, List<IBattleChar> characters) {
        this.scene = scene;
        this.characters = characters;
        processTexts();
    }

    public Scene getScene() {
        return scene;
    }

    public List<IBattleChar> getCharacters() {
        return characters;
    }

    public IBattleChar getSelectedChar() {
        return selectedChar;
    }

    public String getSelectedName() {
        return selectedName;
    }

    public void update() {
        processTexts();
    }

    private void processTexts() {
        String csene = scene.getBaseScene();

        Pattern pattern = Pattern.compile("%.*?%");
        Matcher matcher = pattern.matcher(scene.getBaseScene());

        List<Iterator<? extends Action>> actions = new ArrayList<>();

        for (IBattleChar hero: characters) {
            actions.add(Stream.concat(
                    hero.getActions().stream()
                    , hero.getMagicActions().stream()
            ).collect(Collectors.toList()).iterator());
        }

        Pattern patternDigit = Pattern.compile("\\d");

        while (matcher.find()) {
            String g = matcher.group();
            String replace = "";

            Matcher digitMatcher = patternDigit.matcher(g);
            digitMatcher.find();
            int start = digitMatcher.start();
            int position = Integer.valueOf(digitMatcher.group());

            String cmdName = g.substring(1, start);

            IBattleChar hero = characters.get(position);

            switch (cmdName) {
                //hero
                case HERO_NAME:
                    replace = hero.getName();
                    break;
                case HERO_STRENGTH:
                    replace = String.valueOf(hero.getSkills().get(Skill.STRENGTH).getPoints());
                    break;
                case HERO_ABILITY:
                    replace = String.valueOf(hero.getSkills().get(Skill.ABILITY).getPoints());
                    break;
                case HERO_RESISTANCE:
                    replace = String.valueOf(hero.getSkills().get(Skill.RESISTANCE).getPoints());
                    break;
                case HERO_INTELLIGENCE:
                    replace = String.valueOf(hero.getSkills().get(Skill.INTELLIGENCE).getPoints());
                    break;
                case ACTION:
                    Iterator<? extends Action> iterator = actions.get(position);
                    if (iterator.hasNext()) {
                        replace = iterator.next().getName();
                    }
                    break;
            }

            if (g.length() > replace.length()) {
                replace = StringUtils.padRight(replace, g.length());
            }

            csene = csene.replaceFirst(g, replace);
        }

        scene.setScene(csene);
    }

    public boolean isFinished(String input) {
        try {
            int val = Integer.valueOf(input) - 1;

            if (val > -1 && getCharacters().size() > val) {
                selectedChar = (IBattleChar) getCharacters().get(val).copy();

                System.out.println("Whats your characters name? (max: 15)");
                Scanner scanner = new Scanner(System.in);
                while(scanner.hasNext()) {
                    String name = scanner.next();
                    if (name.length() <= 15) {
                        selectedName = name;
                        break;
                    }
                }
            }

            return true;
        } catch (NumberFormatException e) { }

        return false;
    }
}
