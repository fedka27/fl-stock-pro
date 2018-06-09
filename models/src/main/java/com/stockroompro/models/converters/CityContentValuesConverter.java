package com.stockroompro.models.converters;

import android.content.ContentValues;

import com.artjoker.core.network.AbstractContentValuesConverter;
import com.stockroompro.models.contracts.CityContract;
import com.stockroompro.models.location.City;

/**
 * Created by bagach.alexandr on 05.05.15.
 */
public class CityContentValuesConverter extends AbstractContentValuesConverter<City> {

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CityContract.ID, getObjectModel().getId());
        contentValues.put(CityContract.NAME, getObjectModel().getName());
        contentValues.put(CityContract.REGION_ID, getObjectModel().getRegionId());
        contentValues.put(CityContract.CREATED_AT, getObjectModel().getCreatedAt());
        contentValues.put(CityContract.UPDATED_AT, getObjectModel().getUpdatedAt());
        return contentValues;
    }

    @Override
    public String getUpdateWhere() {
        StringBuilder builder = new StringBuilder();
        builder.append(CityContract.ID);
        builder.append(" = ? ");
        return builder.toString();
    }

    @Override
    public String[] getUpdateArgs() {
        return new String[]{String.valueOf(getObjectModel().getId())};
    }
}

