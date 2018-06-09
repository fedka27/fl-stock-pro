package com.stockroompro.models.converters;

import android.content.ContentValues;

import com.artjoker.core.network.AbstractContentValuesConverter;
import com.artjoker.tool.core.SystemHelper;
import com.stockroompro.models.UserData;
import com.stockroompro.models.contracts.UserDataContract;

/**
 * Created by bagach.alexandr on 27.04.15.
 */
public class UserDataContentValuesConverter extends AbstractContentValuesConverter<UserData> {


    //TODO: fixed created_at and updated_at
    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserDataContract.ID, getObjectModel().getId());
        contentValues.put(UserDataContract.SERVICE_ID, getObjectModel().getServiceId());
        contentValues.put(UserDataContract.FIRST_NAME, getObjectModel().getFirstName());
        contentValues.put(UserDataContract.LAST_NAME, getObjectModel().getLastName());
        contentValues.put(UserDataContract.EMAIL, getObjectModel().getEmail());
        contentValues.put(UserDataContract.CITY_ID, getObjectModel().getCityId());

        String phones = "";
        for (String phone : getObjectModel().getPhones()) {
            phones = phones + "\n" + phone;
        }

        contentValues.put(UserDataContract.PHONES, phones);
        contentValues.put(UserDataContract.REGISTRATION_DATE, getObjectModel().getRegistrationDate());
        contentValues.put(UserDataContract.NUMBER_UNREAD_MESSAGES, getObjectModel().getNumberUnreadMessages());
        contentValues.put(UserDataContract.PICTURE_URL, getObjectModel().getPictureUrl());
        contentValues.put(UserDataContract.RATING, getObjectModel().getRating());
        contentValues.put(UserDataContract.VOICES, getObjectModel().getVoices());
        contentValues.put(UserDataContract.COUNTRY_NAME, getObjectModel().getCountryName());
        contentValues.put(UserDataContract.REGION_NAME, getObjectModel().getRegionName());
        contentValues.put(UserDataContract.CITY_NAME, getObjectModel().getCityName());
        return contentValues;
    }

    @Override
    public String getUpdateWhere() {
        StringBuilder builder = new StringBuilder();
        builder.append(UserDataContract.ID);
        builder.append(" = ? ");
        return builder.toString();
    }

    @Override
    public String[] getUpdateArgs() {
        return new String[]{String.valueOf(getObjectModel().getId())};
    }

}
