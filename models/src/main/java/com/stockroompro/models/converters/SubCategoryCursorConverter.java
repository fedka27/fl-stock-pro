package com.stockroompro.models.converters;

import android.database.Cursor;

import com.artjoker.core.database.AbstractCursorConverter;
import com.stockroompro.models.Category;
import com.stockroompro.models.columns.CategoryColumns;

/**
 * Created by bagach.alexandr on 25.03.15.
 */
public class SubCategoryCursorConverter extends AbstractCursorConverter<Category> {

    private int COLUMN_INDEX_ID;
    private int COLUMN_INDEX_NATIVE_ID;
    private int COLUMN_INDEX_CATEGORY_ID;
    private int COLUMN_INDEX_NAME;
    private int COLUMN_INDEX_SYSTEM_NAME;
    private int COLUMN_INDEX_DESCRIPTION;
    private int COLUMN_INDEX_PARENT_ID;
    private int COLUMN_INDEX_CREATED_AT;
    private int COLUMN_INDEX_UPDATED_AT;

    @Override
    public void setCursor(Cursor cursor) {
        super.setCursor(cursor);
        if (isValid()) {
            COLUMN_INDEX_ID = cursor.getColumnIndex(CategoryColumns.ID);
            COLUMN_INDEX_CATEGORY_ID = cursor.getColumnIndex(CategoryColumns.ID);
            COLUMN_INDEX_NAME = cursor.getColumnIndex(CategoryColumns.NAME);
            COLUMN_INDEX_SYSTEM_NAME = cursor.getColumnIndex(CategoryColumns.SYSTEM_NAME);
            COLUMN_INDEX_DESCRIPTION = cursor.getColumnIndex(CategoryColumns.DESCRIPTION);
            COLUMN_INDEX_PARENT_ID = cursor.getColumnIndex(CategoryColumns.PARENT_ID);
            COLUMN_INDEX_NATIVE_ID = cursor.getColumnIndex(CategoryColumns._ID);
            COLUMN_INDEX_CREATED_AT = cursor.getColumnIndex(CategoryColumns.CREATED_AT);
            COLUMN_INDEX_UPDATED_AT = cursor.getColumnIndex(CategoryColumns.UPDATED_AT);
        }
    }

    @Override
    public Category getObject() {
        Category category = null;
        if (isValid()) {
            category = new Category();
            category.setId(getCursor().getLong(COLUMN_INDEX_ID));
            category.setName(getCursor().getString(COLUMN_INDEX_NAME));
            category.setSystemName(getCursor().getString(COLUMN_INDEX_SYSTEM_NAME));
            category.setDescription(getCursor().getString(COLUMN_INDEX_DESCRIPTION));
            category.setParentId(getCursor().getLong(COLUMN_INDEX_PARENT_ID));
            category.setCreatedAt(getCursor().getLong(COLUMN_INDEX_CREATED_AT));
            category.setUpdatedAt(getCursor().getLong(COLUMN_INDEX_UPDATED_AT));
        }
        return category;
    }

    public long getCategoryId() {
        if (isValid()) {
            return getCursor().getLong(COLUMN_INDEX_ID);
        }
        return 0;
    }
}
