package com.stockroompro.models.converters;

import android.database.Cursor;

import com.artjoker.core.database.AbstractCursorConverter;
import com.stockroompro.models.AdvertFilters;
import com.stockroompro.models.contracts.AdvertFiltersContract;

/**
 * Created by bagach.alexandr on 27.05.15.
 */
public class AdvertFiltersCursorConverter extends AbstractCursorConverter<AdvertFilters> {

    private int COLUMN_INDEX_NATIVE_ID;
    private int COLUMN_INDEX_ID;
    private int COLUMN_INDEX_ADVERT_ID;
    private int COLUMN_INDEX_FILTER_VALUE_ID;


    @Override
    public void setCursor(Cursor cursor) {
        super.setCursor(cursor);
        if (isValid()) {
            COLUMN_INDEX_NATIVE_ID = cursor.getColumnIndex(AdvertFiltersContract._ID);
            COLUMN_INDEX_ADVERT_ID = cursor.getColumnIndex(AdvertFiltersContract.ID);
            COLUMN_INDEX_ADVERT_ID = cursor.getColumnIndex(AdvertFiltersContract.ADVERT_ID);
            COLUMN_INDEX_FILTER_VALUE_ID = cursor.getColumnIndex(AdvertFiltersContract.FILTER_VALUE_ID);
        }
    }

    @Override
    public AdvertFilters getObject() {
        AdvertFilters advertFilters = null;
        if (isValid()) {
            advertFilters = new AdvertFilters();
            advertFilters.setAdvertId(getCursor().getLong(COLUMN_INDEX_ID));
            advertFilters.setAdvertId(getCursor().getLong(COLUMN_INDEX_ADVERT_ID));
            advertFilters.setFilterValueId(getCursor().getLong(COLUMN_INDEX_FILTER_VALUE_ID));
        }
        return advertFilters;
    }
}
