package com.artjoker.core.gcm.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Push {
    @Expose
    @SerializedName("android_key")
    private final String key;

    public Push(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}