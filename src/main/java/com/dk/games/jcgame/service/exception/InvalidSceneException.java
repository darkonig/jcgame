package com.dk.games.jcgame.service.exception;

public class InvalidSceneException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidSceneException(String message) {
        super(message);
    }

    public InvalidSceneException(String message, Throwable cause) {
        super(message, cause);
    }
}
