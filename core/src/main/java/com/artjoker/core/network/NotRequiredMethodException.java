package com.artjoker.core.network;

public class NotRequiredMethodException extends IllegalStateException {

    public NotRequiredMethodException() {
        super("Not required...");
    }

}