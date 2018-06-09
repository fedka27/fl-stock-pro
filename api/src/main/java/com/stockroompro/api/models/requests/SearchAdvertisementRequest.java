package com.stockroompro.api.models.requests;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

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
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_TYPE;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_USED;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.CATEGORY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.COUNTRY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.QUERY;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.REGION_ID;

/**
 * Created by bagach.alexandr on 03.04.15.
 */
public class SearchAdvertisementRequest extends ButchUpdateArrayDatabaseRequest<ResponseItemHolder<Advertisement>, AdvertisementContentValuesConverter> implements RequestWithNotifications {

    /* start---------------------StockRoomBundleBuilderArgs
    The constants bellow correspond with StockRoomBundleBuilderArgs,
    be careful with changing some of fields*/
    private static final String PRICE_LOW = "PRICE_LOW";
    private static final String PRICE_HIGH = "PRICE_HIGH";
    private static final String FILTER_NEW = "FILTER_NEW";
    private static final String FILTER_USED = "FILTER_USED";
    private static final String FILTER_BUY = "FILTER_BUY";
    /**
     * Type of advertisement
     */
    private static final String FILTER_SELL = "FILTER_SELL";
    /**
     * Type of prices
     */
    private static final String FILTER_SALE = "FILTER_SALE";
    private static final String FILTER_FREE = "FILTER_FREE";
    private static final String FILTER_EXCHANGE = "FILTER_EXCHANGE";
    private static final String FILTER_CITY = "FILTER_CITY";
    private static final String FILTER_MORE_FIELDS = "FILTER_MORE_FIELDS";
    /*end-------------------------StockRoomBundleBuilderArgs*/

    public SearchAdvertisementRequest(Context context, Bundle bundle) {
        super(context, AdvertisementContract.CONTENT_URI, AdvertisementContentValuesConverter.class, bundle);
    }

    @Override
    public ResponseHolder<ResponseItemHolder<Advertisement>> makeRequest(long date) throws Exception {
        RequestParams params = new RequestParams();
        if (getBundle().getLong(CATEGORY_ID, -1l) != -1l) {
            params.addParameter(CATEGORY_ID, getBundle().getLong(CATEGORY_ID));
        }

        if (getBundle().getLong(COUNTRY_ID, 0) != 0) {
            params.addParameter(COUNTRY_ID, getBundle().getLong(COUNTRY_ID, 0));
        }

        if (getBundle().getLong(REGION_ID, 0) != 0) {
            params.addParameter(REGION_ID, getBundle().getLong(REGION_ID, 0));
        }

        if (getBundle().getLong(ADVERT_CITY_ID, 0) != 0) {
            params.addParameter(ADVERT_CITY_ID, getBundle().getLong(ADVERT_CITY_ID, 0));
        }

        if (getBundle().getBoolean(FILTER_NEW, false)) {
            params.addParameter(ADVERT_USED, AdvertisementContract.TYPE_NEW);
        } else if (getBundle().getBoolean(FILTER_USED, false)) {
            params.addParameter(ADVERT_USED, AdvertisementContract.TYPE_USED);
        }

        if (getBundle().getBoolean(FILTER_BUY, false)) {
            params.addParameter(ADVERT_TYPE, Advertisement.ADVERT_TYPE_BUY);
        } else if (getBundle().getBoolean(FILTER_SELL, false)) {
            params.addParameter(ADVERT_TYPE, Advertisement.ADVERT_TYPE_SELL);
        }

        ArrayList<Integer> priceTypes = new ArrayList<>(1);
        if (getBundle().getBoolean(FILTER_EXCHANGE, false)) {
            priceTypes.add(Advertisement.PRICE_TYPE_EXCHANGE);
        }

        if (getBundle().getBoolean(FILTER_FREE, false)) {
            priceTypes.add(Advertisement.PRICE_TYPE_FREE);
        }

        if (getBundle().getBoolean(FILTER_SALE, false)) {
            priceTypes.add(Advertisement.PRICE_TYPE_SELL);
        }

        if (priceTypes.size() > 0 && priceTypes.size() < 3/*Max count of price types*/) {

            //params.addParameter(RequestParams.ParamNames.ADVERT_PRICE_TYPES, priceTypes);
            for (int i = 0; i < priceTypes.size(); i++) {
                params.addParameter(RequestParams.ParamNames.ADVERT_PRICE_TYPES, priceTypes);
            }
        }


        if (getBundle().getDouble(PRICE_LOW, -1d) != -1d && getBundle().getDouble(PRICE_HIGH, -1d) != -1d) {
            params.addParameter(RequestParams.ParamNames.MIN, (int) getBundle().getDouble(PRICE_LOW, -1d));
            params.addParameter(RequestParams.ParamNames.MAX, (int) getBundle().getDouble(PRICE_HIGH, -1d));
        }

        if (getBundle().getString(QUERY, null) != null) {
            params.addParameter(QUERY, getBundle().getString(QUERY));
        }

        ArrayList<Integer> moreFilters = getBundle().getIntegerArrayList(FILTER_MORE_FIELDS);
        if (moreFilters != null && moreFilters.size() > 0) {
            for (int i = 0; i < moreFilters.size(); i++) {
                params.addParameter(RequestParams.ParamNames.ADVERT_FILTERS, moreFilters);
            }
        }

        return Communicator.getAppServer().getAdvertsFromSearch(params
                .buildFiltersJSON(AppServerSpecs.ADVERTISEMENT_PATH));
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
        return R.string.search_advertisement;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
