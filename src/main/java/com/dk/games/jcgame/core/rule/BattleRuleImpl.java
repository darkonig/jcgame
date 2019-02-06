package com.dk.games.jcgame.core.rule;

import com.dk.games.jcgame.constant.GameConstants;
import com.dk.games.jcgame.model.IBattleChar;
import com.dk.games.jcgame.model.Skill;
import com.dk.games.jcgame.model.SkillLevel;
import com.dk.games.jcgame.model.action.Action;
import com.dk.games.jcgame.model.action.MagicAction;

import java.io.Serializable;
import java.util.Random;

public class BattleRuleImpl implements BattleRule, Serializable {
    private static final long serialVersionUID = -3699789647574028855L;

    @Override
    public boolean canAttack(IBattleChar battleChar, int dice) {
        SkillLevel intelligence = battleChar.getSkill(Skill.INTELLIGENCE);
        return intelligence.getPoints() + dice >= GameConstants.DICE_SUCCESS;
    }

    @Override
    public boolean canDodge(Action action, IBattleChar battleChar, int dice) {
        SkillLevel intelligence = battleChar.getSkill(Skill.INTELLIGENCE);
        SkillLevel ability = battleChar.getSkill(Skill.ABILITY);

        if (action != null &&  action instanceof MagicAction) {
            if (intelligence.getPoints() < dice) {
                return false;
            }
        }

        // TODO improve
        // for each 2 ability point you can roll a dice
        if (ability != null) {
            for (int i = 0; i < ability.getPoints() / 2; i++) {
                int x = rollDice();
                if (x > dice) {
                    dice = x;
                }
            }
        }

        // dodge the attack
        return dice >= GameConstants.DICE_SUCCESS + 2;
    }

    int rollDice() {
        return new Random().nextInt(6) + 1;
    }
}
