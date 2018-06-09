package com.stockroompro.models.converters;

import android.content.ContentValues;

import com.artjoker.core.network.AbstractContentValuesConverter;
import com.artjoker.tool.core.SystemHelper;
import com.stockroompro.models.Category;
import com.stockroompro.models.columns.CategoryColumns;

/**
 * Created by bagach.alexandr on 25.03.15.
 */
public class SubCategoryContentValuesConverter extends AbstractContentValuesConverter<Category> {
    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CategoryColumns.ID, getObjectModel().getId());
        contentValues.put(CategoryColumns.NAME, getObjectModel().getName());
        contentValues.put(CategoryColumns.DESCRIPTION, getObjectModel().getDescription());
        contentValues.put(CategoryColumns.SYSTEM_NAME, getObjectModel().getSystemName());
        contentValues.put(CategoryColumns.PARENT_ID, getObjectModel().getParentId());
        contentValues.put(CategoryColumns.CREATED_AT, getObjectModel().getCreatedAt());
        contentValues.put(CategoryColumns.UPDATED_AT, getObjectModel().getUpdatedAt());
        return contentValues;
    }

    @Override
    public String getUpdateWhere() {
        StringBuilder builder = new StringBuilder();
        builder.append(CategoryColumns.ID);
        builder.append(" = ? ");
        return builder.toString();
    }

    @Override
    public String[] getUpdateArgs() {
        return new String[]{String.valueOf(getObjectModel().getId())};
    }
}
