package com.stockroompro.models;

/**
 * Created by bagach.alexandr on 08.04.15.
 */
public class Photo {

    private long advertId;

    private String photoUrl;

    public long getAdvertId() {
        return advertId;
    }

    public void setAdvertId(long advertId) {
        this.advertId = advertId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
