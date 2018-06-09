package com.stockroompro.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alexandr.Bagach on 09.09.2015.
 */
public class ExpiryDate  {

    @Expose
    @SerializedName("expiry_date")
    private long expiryDate;

    public long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(long expiryDate) {
        this.expiryDate = expiryDate;
    }
}
