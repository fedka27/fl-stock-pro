package com.artjoker.core.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseHolder<T> {

    @Expose
    @SerializedName("status")
    private int statusCode;

    @Expose
    @SerializedName("data")
    protected T data;
    @Expose
    @SerializedName("reason")
    private String reason;

    @Expose
    @SerializedName("error_message")
    private String errorMessage;

    public final int getStatusCode() {
        return statusCode;
    }

    public final String getReason() {
        return reason;
    }


    public T getData() {
        return data;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public interface StatusCode {
        int SUCCESSFULL = 0;
        int FAIL = 2;
        int INACTIVE_USER = 2;

    }

    public interface Reasons {
        String BANNED = "banned";
        String NOT_ACTIVE = "not-active";
    }

}