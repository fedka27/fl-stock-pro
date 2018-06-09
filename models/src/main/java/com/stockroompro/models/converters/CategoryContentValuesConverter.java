package com.stockroompro.models.converters;

import android.content.ContentValues;

import com.artjoker.core.network.AbstractContentValuesConverter;
import com.stockroompro.models.Category;
import com.stockroompro.models.columns.CategoryColumns;

/**
 * Created by alexsergienko on 25.03.15.
 */
public class CategoryContentValuesConverter extends AbstractContentValuesConverter<Category> {
    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CategoryColumns.ID, getObjectModel().getId());
        contentValues.put(CategoryColumns.NAME, getObjectModel().getName());
        contentValues.put(CategoryColumns.IMAGE_URL, getObjectModel().getImg());
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
