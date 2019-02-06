package com.dk.games.jcgame.core.scene;

import com.dk.games.jcgame.core.processor.SceneTextProcessor;
import com.dk.games.jcgame.model.IBattleChar;
import com.dk.games.jcgame.model.Player;
import com.dk.games.jcgame.model.Skill;
import com.dk.games.jcgame.model.action.Action;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BaseSceneActions {
    private static final String HERO_NAME = "hero_name";
    private static final String HERO_LEVEL = "level";
    private static final String HERO_HP = "HP";
    private static final String HERO_MAX_HP = "fHP";
    private static final String HERO_STAMINA = "ST";
    private static final String HERO_MAX_STAMINA = "fST";
    private static final String HERO_EXP = "exp";
    private static final String HERO_MAX_EXP = "fExp";

    private static final String HERO_STRENGTH = "str";
    private static final String HERO_ABILITY = "abl";
    private static final String HERO_RESISTANCE = "rst";
    private static final String HERO_INTELLIGENCE = "int";

    private static final String ACTION = "action";


    private final SceneTextProcessor processor;
    private boolean showActionHitPoints = false;
    private Iterator<Action> actions;

    public BaseSceneActions() {
        processor = new SceneTextProcessor();
    }

    public boolean isShowActionHitPoints() {
        return showActionHitPoints;
    }

    public void setShowActionHitPoints(boolean showActionHitPoints) {
        this.showActionHitPoints = showActionHitPoints;
    }

    public void setActions(Iterator<Action> actions) {
        this.actions = actions;
    }

    public Iterator<Action> getActions() {
        return actions;
    }

    public SceneTextProcessor getProcessor() {
        return processor;
    }

    public void addProcessor(IBattleChar hero, boolean enemy) {
        addProcessor(hero, enemy, null);
    }

    public void addProcessor(IBattleChar hero, boolean enemy, Supplier<String> supplierAction) {
        if (actions == null) {
            actions = Stream.concat(
                    hero.getActions().stream()
                    , hero.getMagicActions().stream()
            ).collect(Collectors.toList()).iterator();
        }

        final boolean isPlayer = hero instanceof Player;

        String prefix = (enemy ? "e" : "");

        processor.addProcessor(prefix + HERO_NAME, () -> hero.getName());
        processor.addProcessor(prefix + HERO_STRENGTH, () -> String.valueOf(hero.getSkills().get(Skill.STRENGTH).getPoints()) );
        processor.addProcessor(prefix + HERO_ABILITY, () -> String.valueOf(hero.getSkills().get(Skill.ABILITY).getPoints()) );
        processor.addProcessor(prefix + HERO_RESISTANCE, () -> String.valueOf(hero.getSkills().get(Skill.RESISTANCE).getPoints()) );
        processor.addProcessor(prefix + HERO_INTELLIGENCE, () -> String.valueOf(hero.getSkills().get(Skill.INTELLIGENCE).getPoints()) );
        processor.addProcessor(prefix + HERO_LEVEL, () -> String.valueOf(hero.getLevel()) );
        processor.addProcessor(prefix + HERO_HP, () -> String.valueOf(hero.getHealth()) );
        processor.addProcessor(prefix + HERO_MAX_HP, () -> String.valueOf(hero.getMaxHealth()));
        processor.addProcessor(prefix + HERO_STAMINA, () -> String.valueOf(hero.getStamina()));
        processor.addProcessor(prefix + HERO_MAX_STAMINA, () -> String.valueOf(hero.getMaxStamina()) );
        processor.addProcessor(prefix + HERO_EXP, () -> {
            if (isPlayer) {
                return String.valueOf(((Player) hero).getExperience());
            }
            return "";
        });
        processor.addProcessor(prefix + HERO_MAX_EXP, () -> {
            if (isPlayer) {
                return String.valueOf(((Player) hero).getLvlMaxExp());
            }
            return "";
        });

        if (supplierAction != null) {
            processor.addProcessor(prefix + ACTION, supplierAction);
        } else {
            processor.addProcessor(prefix + ACTION, () -> {
                if (actions.hasNext()) {
                    Action ac = actions.next();
                    return ac.toString() + (showActionHitPoints ? " (Hit: " + ac.getHitPoints() + ")" : "");
                }
                return "";
            });
        }
    }

    public String processTexts(Scene scene) {
        return processor.process(scene.getBaseScene());
    }

}
