package com.stockroompro.models.converters;

import android.database.Cursor;

import com.artjoker.core.database.AbstractCursorConverter;
import com.stockroompro.models.Advertisement;
import com.stockroompro.models.UserData;
import com.stockroompro.models.contracts.AdvertisementContract;

import java.util.ArrayList;

/**
 * Created by bagach.alexandr on 27.04.15.
 */
public class UserDataCursorConverter extends AbstractCursorConverter<UserData> {

    private int COLUMN_INDEX_ID;
    private int COLUMN_INDEX_NATIVE_ID;
    private int COLUMN_INDEX_SERVICE_ID;
    private int COLUMN_INDEX_FIRST_NAME;
    private int COLUMN_INDEX_LAST_NAME;
    private int COLUMN_INDEX_EMAIL;
    private int COLUMN_INDEX_CITY_ID;
    private int COLUMN_INDEX_PHONES;
    private int COLUMN_INDEX_REGISTRATION_DATE;
    private int COLUMN_INDEX_NUMBER_UNREAD_MESSAGES;
    private int COLUMN_INDEX_PICTURE_URL;
    private int COLUMN_INDEX_RATING;
    private int COLUMN_INDEX_VOICES;
    private int COLUMN_INDEX_COUNTRY_NAME;
    private int COLUMN_INDEX_REGION_NAME;
    private int COLUMN_INDEX_CITY_NAME;

    @Override
    public void setCursor(Cursor cursor) {
        super.setCursor(cursor);
        if (isValid()) {
            COLUMN_INDEX_ID = cursor.getColumnIndex(UserData.ID);
            COLUMN_INDEX_NATIVE_ID = cursor.getColumnIndex(UserData._ID);
            COLUMN_INDEX_SERVICE_ID = cursor.getColumnIndex(UserData.SERVICE_ID);
            COLUMN_INDEX_FIRST_NAME = cursor.getColumnIndex(UserData.FIRST_NAME);
            COLUMN_INDEX_LAST_NAME = cursor.getColumnIndex(UserData.LAST_NAME);
            COLUMN_INDEX_EMAIL = cursor.getColumnIndex(UserData.EMAIL);
            COLUMN_INDEX_CITY_ID = cursor.getColumnIndex(UserData.CITY_ID);
            COLUMN_INDEX_PHONES = cursor.getColumnIndex(UserData.PHONES);
            COLUMN_INDEX_REGISTRATION_DATE = cursor.getColumnIndex(UserData.REGISTRATION_DATE);
            COLUMN_INDEX_NUMBER_UNREAD_MESSAGES = cursor.getColumnIndex(UserData.NUMBER_UNREAD_MESSAGES);
            COLUMN_INDEX_PICTURE_URL = cursor.getColumnIndex(UserData.PICTURE_URL);
            COLUMN_INDEX_RATING = cursor.getColumnIndex(UserData.RATING);
            COLUMN_INDEX_VOICES = cursor.getColumnIndex(UserData.VOICES);
            COLUMN_INDEX_COUNTRY_NAME = cursor.getColumnIndex(UserData.COUNTRY_NAME);
            COLUMN_INDEX_REGION_NAME = cursor.getColumnIndex(UserData.REGION_NAME);
            COLUMN_INDEX_CITY_NAME = cursor.getColumnIndex(UserData.CITY_NAME);
        }
    }

    @Override
    public UserData getObject() {
        UserData userData = null;
        if (isValid()) {
            userData = new UserData();
            ArrayList<String> phones = new ArrayList<>();
            userData.setId(getCursor().getLong(COLUMN_INDEX_ID));
            userData.setServiceId(getCursor().getInt(COLUMN_INDEX_SERVICE_ID));
            userData.setFirstName(getCursor().getString(COLUMN_INDEX_FIRST_NAME));
            userData.setLastName(getCursor().getString(COLUMN_INDEX_LAST_NAME));
            userData.setEmail(getCursor().getString(COLUMN_INDEX_EMAIL));
            userData.setCityId(getCursor().getLong(COLUMN_INDEX_CITY_ID));
            phones.add(getCursor().getString(COLUMN_INDEX_PHONES));
            userData.setPhones(phones);
            userData.setRegistrationDate(getCursor().getLong(COLUMN_INDEX_REGISTRATION_DATE));
            userData.setNumberUnreadMessages(getCursor().getLong(COLUMN_INDEX_NUMBER_UNREAD_MESSAGES));
            userData.setPictureUrl(getCursor().getString(COLUMN_INDEX_PICTURE_URL));
            userData.setRating(getCursor().getFloat(COLUMN_INDEX_RATING));
            userData.setVoices(getCursor().getLong(COLUMN_INDEX_VOICES));
            userData.setCountryName(getCursor().getString(COLUMN_INDEX_COUNTRY_NAME));
            userData.setRegionName(getCursor().getString(COLUMN_INDEX_REGION_NAME));
            userData.setCityName(getCursor().getString(COLUMN_INDEX_CITY_NAME));
        }
        return userData;
    }

    public long getAdvertisementId() {
        if (isValid()) {
            return getCursor().getLong(COLUMN_INDEX_ID);
        }
        return 0;
    }
}
