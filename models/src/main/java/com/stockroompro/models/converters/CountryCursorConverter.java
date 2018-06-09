package com.stockroompro.models.converters;

import android.database.Cursor;

import com.artjoker.core.database.AbstractCursorConverter;
import com.stockroompro.models.contracts.CountryContract;
import com.stockroompro.models.location.Country;

/**
 * Created by bagach.alexandr on 05.05.15.
 */
public class CountryCursorConverter extends AbstractCursorConverter<Country> {

    private int COLUMN_INDEX_ID;
    private int COLUMN_INDEX_NATIVE_ID;
    private int COLUMN_INDEX_NAME;
    private int COLUMN_INDEX_CODE;
    private int COLUMN_INDEX_CREATED_AT;
    private int COLUMN_INDEX_UPDATED_AT;

    @Override
    public void setCursor(Cursor cursor) {
        super.setCursor(cursor);
        if (isValid()) {
            COLUMN_INDEX_ID = cursor.getColumnIndex(CountryContract.ID);
            COLUMN_INDEX_NAME = cursor.getColumnIndex(CountryContract.NAME);
            COLUMN_INDEX_CODE = cursor.getColumnIndex(CountryContract.CODE);
            COLUMN_INDEX_NATIVE_ID = cursor.getColumnIndex(CountryContract._ID);
            COLUMN_INDEX_CREATED_AT = cursor.getColumnIndex(CountryContract.CREATED_AT);
            COLUMN_INDEX_UPDATED_AT = cursor.getColumnIndex(CountryContract.UPDATED_AT);
        }
    }

    @Override
    public Country getObject() {
        Country country = null;
        if (isValid()) {
            country = new Country();
            country.setId(getCursor().getLong(COLUMN_INDEX_ID));
            country.setName(getCursor().getString(COLUMN_INDEX_NAME));
            country.setCode(getCursor().getString(COLUMN_INDEX_CODE));
            country.setCreatedAt(getCursor().getLong(COLUMN_INDEX_CREATED_AT));
            country.setUpdatedAt(getCursor().getLong(COLUMN_INDEX_UPDATED_AT));
        }
        return country;
    }

    public long getCategoryId() {
        if (isValid()) {
            return getCursor().getLong(COLUMN_INDEX_ID);
        }
        return 0;
    }
}
