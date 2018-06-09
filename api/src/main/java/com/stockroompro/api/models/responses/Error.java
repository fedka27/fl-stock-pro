package com.stockroompro.api.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bagach.alexandr on 01.04.15.
 */
public class Error {

    @Expose
    private long status;

    @Expose
    @SerializedName("error_message")
    private String errorMessage;

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
