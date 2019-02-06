package com.dk.games.jcgame.model.action;

import com.dk.games.jcgame.model.Copy;

import java.io.Serializable;
import java.util.Objects;

public abstract class Action implements Serializable, Copy<Action>, Comparable<Action> {

    private static final long serialVersionUID = -7294290456262430724L;

    private String name;

    public Action(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract int getHitPoints();

    public abstract void setHitPoints(int points);

    public abstract int getStartHitPoints();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return Objects.equals(name, action.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Action a) {
        return getName().compareTo(a.getName());
    }

    @Override
    public Action copy() {
        try {
            return (Action) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
