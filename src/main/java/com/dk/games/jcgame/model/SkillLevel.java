package com.dk.games.jcgame.model;

import java.io.Serializable;
import java.util.Objects;

public class SkillLevel implements Serializable, Copy<SkillLevel> {
    private static final long serialVersionUID = -6022266890784373522L;

    private Skill skill;

    private int points;

    private int startPoints;

    public SkillLevel(Skill skill, int points) {
        this.skill = skill;
        this.points = points;
        this.startPoints = points;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getStartPoints() {
        return startPoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkillLevel that = (SkillLevel) o;
        return skill == that.skill;
    }

    @Override
    public int hashCode() {
        return Objects.hash(skill);
    }

    @Override
    public SkillLevel copy() {
        try {
            return (SkillLevel) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
