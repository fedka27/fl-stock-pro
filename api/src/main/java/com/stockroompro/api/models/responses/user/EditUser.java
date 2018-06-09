package com.stockroompro.api.models.responses.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bagach.alexandr on 25.03.15.
 */
public class EditUser {

    @Expose
    private long id;

    @Expose
    @SerializedName("reg_date")
    private long registrationDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(long registrationDate) {
        this.registrationDate = registrationDate;
    }
}
