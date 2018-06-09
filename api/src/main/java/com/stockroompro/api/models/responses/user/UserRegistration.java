package com.stockroompro.api.models.responses.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by bagach.alexandr on 25.03.15.
 */
public class UserRegistration {

    @Expose
    private long id;

    @Expose
    private String code;

    @Expose
    @SerializedName("reg_date")
    private long registrationDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(long registrationDate) {
        this.registrationDate = registrationDate;
    }
}
