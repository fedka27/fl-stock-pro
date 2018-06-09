package com.stockroompro.models.converters;

import android.content.ContentValues;

import com.artjoker.core.network.AbstractContentValuesConverter;
import com.artjoker.tool.core.SystemHelper;
import com.stockroompro.models.FilterValues;
import com.stockroompro.models.columns.FiltersColumns;

/**
 * Created by bagach.alexandr on 24.04.15.
 */
public class FiltersValuesContentValuesConverter extends AbstractContentValuesConverter<FilterValues.IdAndValue> {

    private long filterId;
    private long createdAt;
    private long updatedAt;

    public void setFilterId(long parentId) {
        this.filterId = parentId;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FiltersColumns.ID, getObjectModel().getId());
        contentValues.put(FiltersColumns.NAME, getObjectModel().getValue());
        contentValues.put(FiltersColumns.FILTER_ID, filterId);
        contentValues.put(FiltersColumns.CREATED_AT, createdAt);
        contentValues.put(FiltersColumns.UPDATED_AT, updatedAt);
        return contentValues;
    }

    @Override
    public String getUpdateWhere() {
        StringBuilder builder = new StringBuilder();
        builder.append(FiltersColumns.ID);
        builder.append(" = ? ");
        return builder.toString();
    }

    @Override
    public String[] getUpdateArgs() {
        return new String[]{
                String.valueOf(getObjectModel().getId()),
        };
    }
}

