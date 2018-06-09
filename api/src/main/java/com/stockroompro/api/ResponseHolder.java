package com.stockroompro.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bagach.alexandr on 25.03.15.
 */
public final class ResponseHolder<T> {

    @Expose
    @SerializedName("status")
    private int statusCode;

    @Expose
    private T data;

    public final int getStatusCode() {
        return statusCode;
    }

    public final T getData() {
        return data;
    }


}
