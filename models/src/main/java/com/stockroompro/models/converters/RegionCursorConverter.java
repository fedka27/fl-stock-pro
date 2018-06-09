package com.stockroompro.models.converters;

import android.database.Cursor;

import com.artjoker.core.database.AbstractCursorConverter;
import com.stockroompro.models.contracts.RegionContract;
import com.stockroompro.models.location.Region;

/**
 * Created by bagach.alexandr on 05.05.15.
 */
public class RegionCursorConverter extends AbstractCursorConverter<Region> {

    private int COLUMN_INDEX_ID;
    private int COLUMN_INDEX_NATIVE_ID;
    private int COLUMN_INDEX_NAME;
    private int COLUMN_INDEX_COUNTRY_ID;
    private int COLUNM_INDEX_CREATED_AT;
    private int COLUMN_INDEX_UPDATED_AT;

    @Override
    public void setCursor(Cursor cursor) {
        super.setCursor(cursor);
        if (isValid()) {
            COLUMN_INDEX_ID = cursor.getColumnIndex(RegionContract.ID);
            COLUMN_INDEX_NAME = cursor.getColumnIndex(RegionContract.NAME);
            COLUMN_INDEX_COUNTRY_ID = cursor.getColumnIndex(RegionContract.COUNTRY_ID);
            COLUMN_INDEX_NATIVE_ID = cursor.getColumnIndex(RegionContract._ID);
            COLUNM_INDEX_CREATED_AT = cursor.getColumnIndex(RegionContract.CREATED_AT);
            COLUMN_INDEX_UPDATED_AT = cursor.getColumnIndex(RegionContract.UPDATED_AT);

        }
    }

    @Override
    public Region getObject() {
        Region region = null;
        if (isValid()) {
            region = new Region();
            region.setId(getCursor().getLong(COLUMN_INDEX_ID));
            region.setName(getCursor().getString(COLUMN_INDEX_NAME));
            region.setCountryId(getCursor().getString(COLUMN_INDEX_COUNTRY_ID));
            region.setCreatedAt(getCursor().getLong(COLUNM_INDEX_CREATED_AT));
            region.setUpdatedAt(getCursor().getLong(COLUMN_INDEX_UPDATED_AT));
        }
        return region;
    }

    public long getCategoryId() {
        if (isValid()) {
            return getCursor().getLong(COLUMN_INDEX_ID);
        }
        return 0;
    }
}
