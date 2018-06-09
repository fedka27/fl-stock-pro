package com.stockroompro.api.models.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateParam {

    @Expose
    @SerializedName("date")
    private final long date;

    public DateParam(long date) {
        this.date = date;
    }

    public long getDate() {
        return date;
    }
}