package com.stockroompro.models.converters;

import android.database.Cursor;
import android.text.TextUtils;

import com.artjoker.core.database.AbstractCursorConverter;
import com.google.android.gms.drive.internal.CloseContentsAndUpdateMetadataRequest;
import com.stockroompro.models.Advertisement;
import com.stockroompro.models.contracts.AdvertFiltersContract;
import com.stockroompro.models.contracts.AdvertisementContract;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by alexsergienko on 23.03.15.
 */
public class AdvertisementCursorConverter extends AbstractCursorConverter<Advertisement> {

    private int COLUMN_INDEX_ID;
    private int COLUMN_INDEX_NATIVE_ID;
    private int COLUMN_INDEX_TITLE;
    private int COLUMN_INDEX_DESCRIPTION;
    private int COLUMN_INDEX_USER_ID;
    private int COLUMN_INDEX_CATEGORY_ID;
    private int COLUMN_INDEX_CATEGORY_NAME;
    private int COLUMN_INDEX_PRICE;
    private int COLUMN_INDEX_PRICE_TYPE;
    private int COLUMN_INDEX_COUNTRY_ID;
    private int COLUMN_INDEX_REGION_ID;
    private int COLUMN_INDEX_CITY_ID;
    private int COLUMN_INDEX_COUNTRY_NAME;
    private int COLUMN_INDEX_REGION_NAME;
    private int COLUMN_INDEX_CITY_NAME;
    private int COLUMN_INDEX_USED;
    private int COLUMN_INDEX_BARGAIN;
    private int COLUMN_INDEX_ACTIVE;
    private int COLUMN_INDEX_APPROVED;
    private int COLUMN_INDEX_EXPIRED;
    private int COLUMN_INDEX_CREATE_DATE;
    private int COLUMN_INDEX_RENEWAL_DATE;
    private int COLUMN_INDEX_EXPIRY_DATE;
    private int COLUMN_INDEX_PHOTO_URL;
    private int COLUMN_INDEX_FAVOURITE;
    private int COLUMN_INDEX_CURRENCY_ID;
    private int COLUMN_INDEX_PHONE;
    private int COLUMN_INDEX_TYPE;
    private int COLUMN_INDEX_FILTERS_NAMES;
    private int COLUMN_INDEX_PATH;
    private int COLUMN_INDEX_CREATE_AT;
    private int COLUMN_INDEX_UPDATED_AT;


    @Override
    public void setCursor(Cursor cursor) {
        super.setCursor(cursor);
        if (isValid()) {
            COLUMN_INDEX_ID = cursor.getColumnIndex(AdvertisementContract.ID);
            COLUMN_INDEX_NATIVE_ID = cursor.getColumnIndex(AdvertisementContract._ID);
            COLUMN_INDEX_TITLE = cursor.getColumnIndex(AdvertisementContract.TITLE);
            COLUMN_INDEX_DESCRIPTION = cursor.getColumnIndex(AdvertisementContract.DESCRIPTION);
            COLUMN_INDEX_USER_ID = cursor.getColumnIndex(AdvertisementContract.USER_ID);
            COLUMN_INDEX_CATEGORY_ID = cursor.getColumnIndex(AdvertisementContract.CATEGORY_ID);
            COLUMN_INDEX_CATEGORY_NAME = cursor.getColumnIndex(AdvertisementContract.CATEGORY_NAME);
            COLUMN_INDEX_PRICE = cursor.getColumnIndex(AdvertisementContract.PRICE);
            COLUMN_INDEX_PRICE_TYPE = cursor.getColumnIndex(AdvertisementContract.PRICE_TYPE);
            COLUMN_INDEX_CURRENCY_ID = cursor.getColumnIndex(AdvertisementContract.CURRENCY_ID);
            COLUMN_INDEX_PHONE = cursor.getColumnIndex(AdvertisementContract.PHONE);
            COLUMN_INDEX_COUNTRY_ID = cursor.getColumnIndex(AdvertisementContract.COUNTRY_ID);
            COLUMN_INDEX_REGION_ID = cursor.getColumnIndex(AdvertisementContract.REGION_ID);
            COLUMN_INDEX_CITY_ID = cursor.getColumnIndex(AdvertisementContract.CITY_ID);
            COLUMN_INDEX_COUNTRY_NAME = cursor.getColumnIndex(AdvertisementContract.COUNTRY_NAME);
            COLUMN_INDEX_REGION_NAME = cursor.getColumnIndex(AdvertisementContract.REGION_NAME);
            COLUMN_INDEX_CITY_NAME = cursor.getColumnIndex(AdvertisementContract.CITY_NAME);
            COLUMN_INDEX_USED = cursor.getColumnIndex(AdvertisementContract.USED);
            COLUMN_INDEX_BARGAIN = cursor.getColumnIndex(AdvertisementContract.BARGAIN);
            COLUMN_INDEX_ACTIVE = cursor.getColumnIndex(AdvertisementContract.ACTIVE);
            COLUMN_INDEX_APPROVED = cursor.getColumnIndex(AdvertisementContract.APPROVED);
            COLUMN_INDEX_EXPIRED = cursor.getColumnIndex(AdvertisementContract.EXPIRED);
            COLUMN_INDEX_RENEWAL_DATE = cursor.getColumnIndex(AdvertisementContract.RENEWAL_DATE);
            COLUMN_INDEX_EXPIRY_DATE = cursor.getColumnIndex(AdvertisementContract.EXPIRY_DATE);
            COLUMN_INDEX_PHOTO_URL = cursor.getColumnIndex(AdvertisementContract.PHOTO_URL);
            COLUMN_INDEX_FAVOURITE = cursor.getColumnIndex(AdvertisementContract.FAVOURITE);
            COLUMN_INDEX_TYPE = cursor.getColumnIndex(AdvertisementContract.TYPE);
            COLUMN_INDEX_FILTERS_NAMES = cursor.getColumnIndex(AdvertisementContract.FILTERS_NAMES);
            COLUMN_INDEX_CREATE_AT = cursor.getColumnIndex(AdvertisementContract.CREATED_AT);
            COLUMN_INDEX_UPDATED_AT = cursor.getColumnIndex(AdvertisementContract.UPDATED_AT);
            COLUMN_INDEX_PATH = cursor.getColumnIndex(AdvertisementContract.PATH);
        }
    }

    @Override
    public Advertisement getObject() {
        Advertisement advertisement = null;
        if (isValid()) {
            advertisement = new Advertisement();
            advertisement.setId(getCursor().getLong(COLUMN_INDEX_ID));
            advertisement.setTitle(getCursor().getString(COLUMN_INDEX_TITLE));
            advertisement.setDescription(getCursor().getString(COLUMN_INDEX_DESCRIPTION));
            advertisement.setUserId(getCursor().getLong(COLUMN_INDEX_USER_ID));
            advertisement.setCategoryId(getCursor().getLong(COLUMN_INDEX_CATEGORY_ID));
            advertisement.setCategoryName(getCursor().getString(COLUMN_INDEX_CATEGORY_NAME));
            advertisement.setPrice(getCursor().getFloat(COLUMN_INDEX_PRICE));
            advertisement.setCurrencyId(getCursor().getInt(COLUMN_INDEX_CURRENCY_ID));
            advertisement.setPriceType(getCursor().getInt(COLUMN_INDEX_PRICE_TYPE));
            advertisement.setCountryId(getCursor().getLong(COLUMN_INDEX_COUNTRY_ID));
            advertisement.setRegionId(getCursor().getLong(COLUMN_INDEX_REGION_ID));
            advertisement.setCityId(getCursor().getLong(COLUMN_INDEX_CITY_ID));
            advertisement.setCountryName(getCursor().getString(COLUMN_INDEX_COUNTRY_NAME));
            advertisement.setRegionName(getCursor().getString(COLUMN_INDEX_REGION_NAME));
            advertisement.setCityName(getCursor().getString(COLUMN_INDEX_CITY_NAME));
            advertisement.setUsed(getCursor().getInt(COLUMN_INDEX_USED));
            advertisement.setBargain(getCursor().getInt(COLUMN_INDEX_BARGAIN));
            advertisement.setActive(getCursor().getInt(COLUMN_INDEX_ACTIVE));
            advertisement.setApproved(getCursor().getInt(COLUMN_INDEX_APPROVED));
            advertisement.setExpired(getCursor().getInt(COLUMN_INDEX_EXPIRED));
            advertisement.setRenewalDate(getCursor().getLong(COLUMN_INDEX_RENEWAL_DATE));
            advertisement.setExpiryDate(getCursor().getLong(COLUMN_INDEX_EXPIRY_DATE));
            advertisement.setPhotoUrl(getCursor().getString(COLUMN_INDEX_PHOTO_URL));
            advertisement.setIsFavourite(getCursor().getInt(COLUMN_INDEX_FAVOURITE));
            advertisement.setType(getCursor().getLong(COLUMN_INDEX_TYPE));
            advertisement.setPhones(getPhones(getCursor().getString(COLUMN_INDEX_PHONE)));
            advertisement.setFiltersNames(getFiltersNames(getCursor().getString(COLUMN_INDEX_FILTERS_NAMES)));
            advertisement.setPath(getCursor().getString(COLUMN_INDEX_PATH));
            advertisement.setCreatedAt(getCursor().getLong(COLUMN_INDEX_CREATE_AT));
            advertisement.setUpdatedAt(getCursor().getLong(COLUMN_INDEX_UPDATED_AT));
        }
        return advertisement;
    }

    private ArrayList<String> getPhones(String phones) {
        ArrayList<String> phonesList = new ArrayList<>();
        if (!TextUtils.isEmpty(phones)) {
            String[] phonesArray = phones.split(",");
            Collections.addAll(phonesList, phonesArray);
        }
        return phonesList;
    }

    private ArrayList<String> getFiltersNames(String filters) {
        ArrayList<String> filterNames = new ArrayList<>();
        if (!TextUtils.isEmpty(filters)) {
            String[] phonesArray = filters.split(",");
            Collections.addAll(filterNames, phonesArray);
        }
        return filterNames;
    }

    public long getAdvertisementId() {
        if (isValid()) {
            return getCursor().getLong(COLUMN_INDEX_ID);
        }
        return 0;
    }

    public long getAdvertisementUserId() {
        if (isValid()) {
            return getCursor().getLong(COLUMN_INDEX_USER_ID);
        }
        return 0;
    }
}
