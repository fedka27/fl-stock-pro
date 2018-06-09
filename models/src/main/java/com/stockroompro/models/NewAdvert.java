package com.stockroompro.models;

import com.google.gson.annotations.Expose;

/**
 * Created by bagach.alexandr on 14.05.15.
 */
public class NewAdvert {

    @Expose
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
