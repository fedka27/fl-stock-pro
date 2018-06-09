package com.stockroompro.models.converters;

import android.content.ContentValues;

import com.artjoker.core.network.AbstractContentValuesConverter;
import com.stockroompro.models.contracts.CountryContract;
import com.stockroompro.models.location.Country;

/**
 * Created by bagach.alexandr on 05.05.15.
 */
public class CountryContentValuesConverter extends AbstractContentValuesConverter<Country> {

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CountryContract.ID, getObjectModel().getId());
        contentValues.put(CountryContract.NAME, getObjectModel().getName());
        contentValues.put(CountryContract.CODE, getObjectModel().getCode());
        contentValues.put(CountryContract.CREATED_AT, getObjectModel().getCreatedAt());
        contentValues.put(CountryContract.UPDATED_AT, getObjectModel().getUpdatedAt());
        return contentValues;
    }

    @Override
    public String getUpdateWhere() {
        StringBuilder builder = new StringBuilder();
        builder.append(CountryContract.ID);
        builder.append(" = ? ");
        return builder.toString();
    }

    @Override
    public String[] getUpdateArgs() {
        return new String[]{String.valueOf(getObjectModel().getId())};
    }

}
