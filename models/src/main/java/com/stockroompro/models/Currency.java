package com.stockroompro.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bagach.alexandr on 25.03.15.
 */
public class Currency {

    @Expose
    private long id;

    @Expose
    private String name;

    @Expose
    @SerializedName("ratio_by_default")
    private double ratioByDefault;

    public Currency(long id, String name, double ratioByDefault) {
        this.id = id;
        this.name = name;
        this.ratioByDefault = ratioByDefault;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRatioByDefault() {
        return ratioByDefault;
    }

    public void setRatioByDefault(double ratioByDefault) {
        this.ratioByDefault = ratioByDefault;
    }

    @Override
    public String toString() {
        return name;
    }
}
