package com.stockroompro.models.converters;

import android.content.ContentValues;

import com.artjoker.core.network.AbstractContentValuesConverter;
import com.artjoker.tool.core.SystemHelper;
import com.stockroompro.models.Advertisement;
import com.stockroompro.models.contracts.AdvertisementContract;

import java.util.ArrayList;

/**
 * Created by bagach.alexandr on 03.04.15.
 */
public class AdvertisementContentValuesConverter extends AbstractContentValuesConverter<Advertisement> {


    //TODO: fixed created_at and updated_at
    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(AdvertisementContract.ID, getObjectModel().getId());
        contentValues.put(AdvertisementContract.TITLE, getObjectModel().getTitle());
        contentValues.put(AdvertisementContract.DESCRIPTION, getObjectModel().getDescription());
        contentValues.put(AdvertisementContract.USER_ID, getObjectModel().getUserId());
        contentValues.put(AdvertisementContract.CATEGORY_ID, getObjectModel().getCategoryId());
        contentValues.put(AdvertisementContract.CATEGORY_NAME, getObjectModel().getCategoryName());
        contentValues.put(AdvertisementContract.PRICE, getObjectModel().getPrice());
        contentValues.put(AdvertisementContract.PRICE_TYPE, getObjectModel().getPriceType());
        contentValues.put(AdvertisementContract.CURRENCY_ID, getObjectModel().getCurrencyId());
        contentValues.put(AdvertisementContract.COUNTRY_ID, getObjectModel().getCountryId());
        contentValues.put(AdvertisementContract.REGION_ID, getObjectModel().getRegionId());
        contentValues.put(AdvertisementContract.CITY_ID, getObjectModel().getCityId());
        contentValues.put(AdvertisementContract.COUNTRY_NAME, getObjectModel().getCountryName());
        contentValues.put(AdvertisementContract.REGION_NAME, getObjectModel().getRegionName());
        contentValues.put(AdvertisementContract.CITY_NAME, getObjectModel().getCityName());
        contentValues.put(AdvertisementContract.USED, getObjectModel().getUsed());
        contentValues.put(AdvertisementContract.BARGAIN, getObjectModel().getBargain());
        contentValues.put(AdvertisementContract.ACTIVE, getObjectModel().getActive());
        contentValues.put(AdvertisementContract.APPROVED, getObjectModel().getApproved());
        contentValues.put(AdvertisementContract.EXPIRED, getObjectModel().getExpired());
        contentValues.put(AdvertisementContract.RENEWAL_DATE, getObjectModel().getRenewalDate());
        contentValues.put(AdvertisementContract.EXPIRY_DATE, getObjectModel().getExpiryDate());
        contentValues.put(AdvertisementContract.PHOTO_URL, (getObjectModel().getPhotos() != null && getObjectModel().getPhotos().size() > 0) ? getObjectModel().getPhotos().get(0) : "null");
        contentValues.put(AdvertisementContract.CREATED_AT, getObjectModel().getCreatedAt());
        contentValues.put(AdvertisementContract.UPDATED_AT, getObjectModel().getUpdatedAt());
        contentValues.put(AdvertisementContract.PATH, getObjectModel().getPath());
        contentValues.put(AdvertisementContract.FAVOURITE, getObjectModel().isFavourite());
        contentValues.put(AdvertisementContract.PHONE, (getObjectModel().getPhones() != null && getObjectModel().getPhones().size() > 0) ?
                setPhones(getObjectModel().getPhones()) : null);
        contentValues.put(AdvertisementContract.FILTERS_NAMES, (getObjectModel().getFiltersNames() != null && getObjectModel().getFiltersNames().size() > 0) ?
                setFiltersNames(getObjectModel().getFiltersNames()) : null);
        contentValues.put(AdvertisementContract.TYPE, getObjectModel().getType());
        return contentValues;
    }

    private String setPhones(ArrayList<String> phonesList) {
        StringBuilder builder = new StringBuilder();
        for (String phone : phonesList) {
            builder.append(phone);
            builder.append(",");
        }
        return builder.toString();
    }

    private String setFiltersNames(ArrayList<String> filters) {
        StringBuilder builder = new StringBuilder();
        for (String filterName : filters) {
            builder.append(filterName);
            builder.append(",");
        }
        return builder.toString();
    }


    @Override
    public String getUpdateWhere() {
        StringBuilder builder = new StringBuilder();
        builder.append(AdvertisementContract.ID);
        builder.append(" = ? ");
        return builder.toString();
    }

    @Override
    public String[] getUpdateArgs() {
        return new String[]{String.valueOf(getObjectModel().getId())};
    }


}
