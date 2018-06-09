package com.stockroompro.models.converters;

import android.content.ContentValues;

import com.artjoker.core.network.AbstractContentValuesConverter;
import com.stockroompro.models.contracts.RegionContract;
import com.stockroompro.models.location.Region;

/**
 * Created by bagach.alexandr on 05.05.15.
 */
public class RegionContentValuesConverter extends AbstractContentValuesConverter<Region> {

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RegionContract.ID, getObjectModel().getId());
        contentValues.put(RegionContract.NAME, getObjectModel().getName());
        contentValues.put(RegionContract.COUNTRY_ID, getObjectModel().getCountryId());
        contentValues.put(RegionContract.CREATED_AT, getObjectModel().getCreatedAt());
        contentValues.put(RegionContract.UPDATED_AT, getObjectModel().getUpdatedAt());
        return contentValues;
    }

    @Override
    public String getUpdateWhere() {
        StringBuilder builder = new StringBuilder();
        builder.append(RegionContract.ID);
        builder.append(" = ? ");
        return builder.toString();
    }

    @Override
    public String[] getUpdateArgs() {
        return new String[]{String.valueOf(getObjectModel().getId())};
    }
}
