package com.dk.games.jcgame.service.exception;

public class SaverException extends Exception {

    private static final long serialVersionUID = 1L;

    public SaverException(String message) {
        super(message);
    }

    public SaverException(String message, Throwable cause) {
        super(message, cause);
    }

}
