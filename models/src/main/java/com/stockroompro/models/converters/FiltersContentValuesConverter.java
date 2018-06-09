package com.stockroompro.models.converters;

import com.artjoker.tool.core.SystemHelper;
import com.google.android.gms.drive.query.Filters;
import com.stockroompro.models.CategoriesFilters;

import android.content.ContentValues;

import com.artjoker.core.network.AbstractContentValuesConverter;
import com.stockroompro.models.FilterValues;
import com.stockroompro.models.columns.FiltersColumns;

/**
 * Created by bagach.alexandr on 23.04.15.
 */
public class FiltersContentValuesConverter extends AbstractContentValuesConverter<FilterValues> {

    private long categoryId;

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FiltersColumns.ID, getObjectModel().getId());
        contentValues.put(FiltersColumns.NAME, getObjectModel().getName());
        contentValues.put(FiltersColumns.CATEGORY_ID, categoryId);
        contentValues.put(FiltersColumns.CREATED_AT, getObjectModel().getCreatedAt());
        contentValues.put(FiltersColumns.UPDATED_AT, getObjectModel().getUpdatedAt());
        return contentValues;
    }

    @Override
    public String getUpdateWhere() {
        StringBuilder builder = new StringBuilder();
        builder.append(FiltersColumns.ID);
        builder.append(" = ? AND ");
        builder.append(FiltersColumns.CATEGORY_ID);
        builder.append(" = ? ");
        return builder.toString();
    }

    @Override
    public String[] getUpdateArgs() {
        return new String[]{String.valueOf(getObjectModel().getId()), String.valueOf(categoryId)};
    }
}
