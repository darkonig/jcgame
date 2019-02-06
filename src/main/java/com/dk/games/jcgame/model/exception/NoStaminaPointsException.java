package com.dk.games.jcgame.model.exception;

public class NoStaminaPointsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NoStaminaPointsException(String message) {
        super(message);
    }

}
