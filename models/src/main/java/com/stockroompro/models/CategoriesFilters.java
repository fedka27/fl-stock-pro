package com.stockroompro.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stockroompro.models.columns.FiltersColumns;

import java.util.ArrayList;

/**
 * Created by bagach.alexandr on 23.04.15.
 */
public class CategoriesFilters implements FiltersColumns{

    @Expose
    @SerializedName(CATEGORY_ID)
    private long categoryId;

    @Expose
    private String name;

    public long getId() {
        return categoryId;
    }

    public void setId(long id) {
        this.categoryId = id;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}