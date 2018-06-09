package com.stockroompro.models;

import com.google.gson.annotations.Expose;

/**
 * Created by bagach.alexandr on 25.03.15.
 */
public class NameAndValue {

    @Expose
    private String name;

    @Expose
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
