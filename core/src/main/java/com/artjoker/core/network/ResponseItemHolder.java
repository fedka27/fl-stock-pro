package com.artjoker.core.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by bagach.alexandr on 01.04.15.
 */
public final class ResponseItemHolder<T> {

    @Expose
    private long amount;

    @Expose
    private ArrayList<T> items;

    @Expose
    @SerializedName("ad_id")
    private int adId;

    @Expose
    private String title;

    @Expose
    private String price;

    @Expose
    @SerializedName("currency_id")
    private long currency;

    @Expose
    private String photo;

    @Expose
    @SerializedName("ad_active")
    private int adActive;

    public final long getAdId() {
        return adId;
    }

    public final long getAmount() {
        return amount;
    }

    public final ArrayList<T> getItems() {
        return items;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getCurrency() {
        return currency;
    }

    public void setCurrency(long currency) {
        this.currency = currency;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getAdActive() {
        return adActive;
    }

    public void setAdActive(int adACtive) {
        this.adActive = adACtive;
    }
}
