package com.stockroompro.models.location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stockroompro.models.columns.CityColumns;

/**
 * Created by bagach.alexandr on 25.03.15.
 */
public class City implements CityColumns {

    @Expose
    private long id;

    @Expose
    @SerializedName(REGION_ID)
    private String regionId;

    @Expose
    private String name;

    @Expose
    @SerializedName(CREATED_AT)
    private long createdAt;

    @Expose
    @SerializedName(UPDATED_AT)
    private long updatedAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
