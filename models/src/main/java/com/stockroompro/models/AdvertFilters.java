package com.stockroompro.models;

/**
 * Created by bagach.alexandr on 27.05.15.
 */
public class AdvertFilters {

    long id;

    long advertId;

    long filterValueId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAdvertId() {
        return advertId;
    }

    public void setAdvertId(long advertId) {
        this.advertId = advertId;
    }

    public long getFilterValueId() {
        return filterValueId;
    }

    public void setFilterValueId(long filterValueId) {
        this.filterValueId = filterValueId;
    }
}
