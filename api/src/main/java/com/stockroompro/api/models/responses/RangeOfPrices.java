package com.stockroompro.api.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alexsergienko on 09.06.15.
 */
public class RangeOfPrices {

    @Expose
    private long amount;
    @Expose
    @SerializedName("price_max")
    private double priceMax;
    @Expose
    @SerializedName("currency_id")
    private int currencyId;

    public final int getCurrencyId() {
        return currencyId;
    }

    public double getPriceMax() {
        return priceMax;
    }

    public final long getAmount() {
        return amount;
    }

}
