package com.dk.games.jcgame.model;

public class BaseChar implements Char {
    private static final long serialVersionUID = -1065859268228224240L;

    private String name;
    private boolean hero;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isHero() {
        return hero;
    }

    public void setHero(boolean hero) {
        this.hero = hero;
    }

    @Override
    public Char copy() {
        try {
            return (Char) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

}
