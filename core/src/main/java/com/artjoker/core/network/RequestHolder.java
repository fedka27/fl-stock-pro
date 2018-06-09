package com.artjoker.core.network;

import com.google.gson.annotations.Expose;

public class RequestHolder<T> {
    @Expose
    private final T data;

    public RequestHolder(final T data) {
        this.data = data;
    }

    public final T getData() {
        return data;
    }


}