package com.stockroompro.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by bagach.alexandr on 14.05.15.
 */
public class Filters {

    @Expose
    @SerializedName("category_id")
    private long categoryId;

    @Expose
    private ArrayList<FilterValues> values;

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public ArrayList<FilterValues> getValues() {
        return values;
    }

    public void setValues(ArrayList<FilterValues> values) {
        this.values = values;
    }
}
