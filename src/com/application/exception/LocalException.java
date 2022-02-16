package com.application.exception;

public class LocalException extends Exception{

    private static final long serialVersionUID = 2135469238419796749L;

    public LocalException() {
        super();
    }

    public LocalException(String message) {
        super(message);
    }
}
