package com.stockroompro.api.models;

import com.google.gson.annotations.Expose;

/**
 * Created by bagach.alexandr on 25.03.15.
 */
public class IdAndValue {

    @Expose
    private long id;

    @Expose
    private String value;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String name) {
        this.value = name;
    }
}
