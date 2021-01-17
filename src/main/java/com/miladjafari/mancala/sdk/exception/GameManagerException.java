package com.miladjafari.mancala.sdk.exception;

public class GameManagerException extends RuntimeException {

    public GameManagerException(Throwable exception) {
        super(exception.getMessage(), exception);
    }

    public GameManagerException(String message) {
        super(message);
    }
}
