package com.dk.games.jcgame.model;

import java.io.Serializable;

public interface Char extends Serializable, Copy<Char> {

    String getName();

    boolean isHero();

}
