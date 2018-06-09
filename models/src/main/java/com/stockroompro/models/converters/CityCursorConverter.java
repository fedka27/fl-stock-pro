package com.stockroompro.models.converters;

import android.database.Cursor;

import com.artjoker.core.database.AbstractCursorConverter;
import com.stockroompro.models.contracts.CityContract;
import com.stockroompro.models.location.City;

/**
 * Created by bagach.alexandr on 05.05.15.
 */
public class CityCursorConverter extends AbstractCursorConverter<City> {

    private int COLUMN_INDEX_ID;
    private int COLUMN_INDEX_NATIVE_ID;
    private int COLUMN_INDEX_NAME;
    private int COLUMN_INDEX_REGION_ID;
    private int COLUMN_INDEX_CREATED_AT;
    private int COLUMN_INDEX_UPDATED_AT;

    @Override
    public void setCursor(Cursor cursor) {
        super.setCursor(cursor);
        if (isValid()) {
            COLUMN_INDEX_ID = cursor.getColumnIndex(CityContract.ID);
            COLUMN_INDEX_NAME = cursor.getColumnIndex(CityContract.NAME);
            COLUMN_INDEX_REGION_ID = cursor.getColumnIndex(CityContract.REGION_ID);
            COLUMN_INDEX_NATIVE_ID = cursor.getColumnIndex(CityContract._ID);
            COLUMN_INDEX_CREATED_AT = cursor.getColumnIndex(CityContract.CREATED_AT);
            COLUMN_INDEX_UPDATED_AT = cursor.getColumnIndex(CityContract.UPDATED_AT);
        }
    }

    @Override
    public City getObject() {
        City city = null;
        if (isValid()) {
            city = new City();
            city.setId(getCursor().getLong(COLUMN_INDEX_ID));
            city.setName(getCursor().getString(COLUMN_INDEX_NAME));
            city.setRegionId(getCursor().getString(COLUMN_INDEX_REGION_ID));
            city.setCreatedAt(getCursor().getLong(COLUMN_INDEX_CREATED_AT));
            city.setUpdatedAt(getCursor().getLong(COLUMN_INDEX_UPDATED_AT));
        }
        return city;
    }

    public long getCategoryId() {
        if (isValid()) {
            return getCursor().getLong(COLUMN_INDEX_ID);
        }
        return 0;
    }
}
