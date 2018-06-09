package com.stockroompro.api.models.requests;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.artjoker.core.network.ButchUpdateArrayDatabaseRequest;
import com.artjoker.core.network.NotificationsPolicy;
import com.artjoker.core.network.RequestWithNotifications;
import com.artjoker.core.network.ResponseHolder;
import com.artjoker.core.network.ResponseItemHolder;
import com.stockroompro.api.AppServerSpecs;
import com.stockroompro.api.Communicator;
import com.stockroompro.api.R;
import com.stockroompro.models.AdvertFilters;
import com.stockroompro.models.Advertisement;
import com.stockroompro.models.columns.CityColumns;
import com.stockroompro.models.columns.CountryColumns;
import com.stockroompro.models.columns.RegionColumns;
import com.stockroompro.models.contracts.AdvertFiltersContract;
import com.stockroompro.models.contracts.AdvertisementContract;
import com.stockroompro.models.contracts.CityContract;
import com.stockroompro.models.contracts.CountryContract;
import com.stockroompro.models.contracts.FilterValuesContract;
import com.stockroompro.models.contracts.PhotosContract;
import com.stockroompro.models.contracts.RegionContract;
import com.stockroompro.models.converters.AdvertFiltersContentValuesConverter;
import com.stockroompro.models.converters.AdvertisementContentValuesConverter;
import com.stockroompro.models.converters.PhotosContentValuesConverter;

import java.util.ArrayList;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_CITY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.CATEGORY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.COUNTRY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.LIMIT;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.OFFSET;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.REGION_ID;

/**
 * Created by bagach.alexandr on 03.04.15.
 */
public class AdvertisementRequest extends ButchUpdateArrayDatabaseRequest<ResponseItemHolder<Advertisement>, AdvertisementContentValuesConverter> implements RequestWithNotifications {

    public AdvertisementRequest(Context context, Bundle bundle) {
        super(context, AdvertisementContract.CONTENT_URI, AdvertisementContentValuesConverter.class, bundle);
    }

    @Override
    public ResponseHolder<ResponseItemHolder<Advertisement>> makeRequest(long date) throws Exception {
        return Communicator.getAppServer().getAdvertsByCategoryIdAndText(
                new RequestParams()
                        .addParameter(CATEGORY_ID, getBundle().getLong(CATEGORY_ID))
                        .addParameter(LIMIT, getBundle().getInt(LIMIT))
                        .addParameter(OFFSET, getBundle().getInt(OFFSET))
                        .addParameter(COUNTRY_ID, getBundle().getLong(COUNTRY_ID, 0))
                        .addParameter(REGION_ID, getBundle().getLong(REGION_ID, 0))
                        .addParameter(ADVERT_CITY_ID, getBundle().getLong(ADVERT_CITY_ID, 0))
                        .buildJSON(AppServerSpecs.ADVERTISEMENT_PATH));
    }

    @Override
    public void processResponse(ResponseHolder<ResponseItemHolder<Advertisement>> responseHolder) throws Exception {
        super.processResponse(responseHolder);
        PhotosContentValuesConverter converter = new PhotosContentValuesConverter();
        AdvertFiltersContentValuesConverter filtersConverter = new AdvertFiltersContentValuesConverter();
        for (Advertisement advertisement : responseHolder.getData().getItems()) {
            getContext().getContentResolver().delete(PhotosContract.CONTENT_URI,
                    PhotosContract.ADVERTISEMENT_ID + " = ?", new String[]{String.valueOf(advertisement.getId())});
            converter.setAdvertisementId(advertisement.getId());
            converter.setCreatedAt(advertisement.getCreatedAt());
            converter.setUpdatedAt(advertisement.getUpdatedAt());
            processItems(getContext().getContentResolver(), PhotosContract.CONTENT_URI, advertisement.getPhotos(), converter);
            getContext().getContentResolver().delete(AdvertFiltersContract.CONTENT_URI,
                    AdvertFiltersContract.ADVERT_ID + " = ?", new String[]{String.valueOf(advertisement.getId())});
            if (advertisement.getFilters() != null) {
                ArrayList<AdvertFilters> advertFiltersArrayList = new ArrayList<>(advertisement.getFilters().size());
                for (long filterValueId : advertisement.getFilters()) {
                    AdvertFilters af = new AdvertFilters();
                    af.setId(getFilterValueId(filterValueId));
                    af.setAdvertId(advertisement.getId());
                    af.setFilterValueId(filterValueId);
                    advertFiltersArrayList.add(af);
                }
                processItems(getContext().getContentResolver(), AdvertFiltersContract.CONTENT_URI, advertFiltersArrayList, filtersConverter);
            }

            saveLocation(advertisement);
        }
    }

    private long getFilterValueId(long filterValueId) {
        Cursor cursor = getContext().getContentResolver().query(FilterValuesContract.CONTENT_URI,
                new String[]{String.valueOf(FilterValuesContract._ID)},
                FilterValuesContract.ID + " = ? ",
                new String[]{String.valueOf(filterValueId)},
                null);
        if (cursor != null && cursor.getCount() > 0) {
            try {
                if (cursor.moveToFirst()) {
                    return cursor.getLong(cursor.getColumnIndex(FilterValuesContract._ID));
                }
            } finally {
                cursor.close();
            }
        }
        return 0;
    }

    private void saveLocation(Advertisement advertisement) {
        ContentValues countryValues = new ContentValues();
        countryValues.put(CountryColumns.ID, advertisement.getCountryId());
        countryValues.put(CountryColumns.NAME, advertisement.getCountryName());
        countryValues.put(CountryColumns.CREATED_AT, 0);
        countryValues.put(CountryColumns.UPDATED_AT, 0);
        getContext().getContentResolver().update(CountryContract.CONTENT_URI, countryValues,
                CountryColumns.ID + " = ? ", new String[]{String.valueOf(advertisement.getCountryId())});

        ContentValues regionValues = new ContentValues();
        regionValues.put(RegionColumns.ID, advertisement.getRegionId());
        regionValues.put(RegionColumns.NAME, advertisement.getRegionName());
        regionValues.put(RegionColumns.COUNTRY_ID, advertisement.getCountryId());
        regionValues.put(RegionColumns.CREATED_AT, 0);
        regionValues.put(RegionColumns.UPDATED_AT, 0);
        getContext().getContentResolver().update(RegionContract.CONTENT_URI, regionValues,
                RegionColumns.ID + " = ? ", new String[]{String.valueOf(advertisement.getRegionId())});

        ContentValues cityValues = new ContentValues();
        cityValues.put(CityColumns.ID, advertisement.getCityId());
        cityValues.put(CityColumns.NAME, advertisement.getCityName());
        cityValues.put(CityColumns.REGION_ID, advertisement.getRegionId());
        cityValues.put(CityColumns.CREATED_AT, 0);
        cityValues.put(CityColumns.UPDATED_AT, 0);
        getContext().getContentResolver().update(CityContract.CONTENT_URI, cityValues,
                CityColumns.ID + " = ? ", new String[]{String.valueOf(advertisement.getCityId())});
    }

    @Override
    public int getNotificationPolicy() {
        return NotificationsPolicy.NOTIFY_IF_SERVER_ERROR_RESPONSE | NotificationsPolicy.NOTIFY_IF_ERROR_PROCESSING | NotificationsPolicy.NOTIFY_SUCCESS | NotificationsPolicy.SHOW_INTERNET_CONNECTION_LOST;
    }


    @Override
    public int getRequestName() {
        return R.string.advertisement;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
