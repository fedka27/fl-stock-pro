package com.stockroompro.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stockroompro.models.columns.FiltersColumns;

import java.util.ArrayList;

/**
 * Created by bagach.alexandr on 24.04.15.
 */
public class FilterValues implements FiltersColumns {

    @Expose
    private long id;

    @Expose
    private String name;

    @Expose
    @SerializedName(CREATED_AT)
    private long createdAt;

    @Expose
    @SerializedName(UPDATED_AT)
    private long updatedAt;

    @Expose
    private ArrayList<IdAndValue> values;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<IdAndValue> getValue() {
        return values;
    }

    public void setValue(ArrayList<IdAndValue> value) {
        this.values = value;
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

    public static class IdAndValue {

        @Expose
        private int id;

        @Expose
        private String value;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String name) {
            this.value = name;
        }
    }
}
