package com.stockroompro.models.converters;

import android.content.ContentValues;

import com.artjoker.core.network.AbstractContentValuesConverter;
import com.stockroompro.models.AdvertFilters;
import com.stockroompro.models.contracts.AdvertFiltersContract;

/**
 * Created by bagach.alexandr on 27.05.15.
 */
public class AdvertFiltersContentValuesConverter extends AbstractContentValuesConverter<AdvertFilters> {

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(AdvertFiltersContract.ID, getObjectModel().getId());
        contentValues.put(AdvertFiltersContract.ADVERT_ID, getObjectModel().getAdvertId());
        contentValues.put(AdvertFiltersContract.FILTER_VALUE_ID, getObjectModel().getFilterValueId());
        return contentValues;
    }

    @Override
    public String getUpdateWhere() {
        StringBuilder builder = new StringBuilder();
        builder.append(AdvertFiltersContract.ID);
        builder.append(" = ? AND ");
        builder.append(AdvertFiltersContract.ADVERT_ID);
        builder.append(" = ? AND ");
        builder.append(AdvertFiltersContract.FILTER_VALUE_ID);
        builder.append(" = ? ");
        return builder.toString();
    }

    @Override
    public String[] getUpdateArgs() {
        return new String[]{String.valueOf(getObjectModel().getId()),
                String.valueOf(getObjectModel().getAdvertId()),
                String.valueOf(getObjectModel().getFilterValueId())};
    }
}
