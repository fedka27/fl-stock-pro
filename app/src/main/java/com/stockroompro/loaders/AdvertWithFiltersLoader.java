package com.stockroompro.loaders;

import android.content.AsyncTaskLoader;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.artjoker.core.database.ContentValuesConverter;
import com.artjoker.core.network.ResponseHolder;
import com.artjoker.core.network.ResponseItemHolder;
import com.stockroompro.api.AppServerSpecs;
import com.stockroompro.api.Communicator;
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
import com.stockroompro.models.converters.AdvertisementCursorConverter;
import com.stockroompro.models.converters.PhotosContentValuesConverter;
import com.stockroompro.utils.AdvertUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Александр on 17.09.2016.
 */
public class AdvertWithFiltersLoader extends AsyncTaskLoader {

    private Bundle bundle;

    public AdvertWithFiltersLoader(Context context, Bundle bundle) {
        super(context);
        this.bundle = bundle;
    }

    @Override
    public Object loadInBackground() {
        try {
            ArrayList<Long> arrayList = processResponse(
                    Communicator
                            .getAppServer()
                            .getAdvertsFromSearch(AdvertUtils.createParams(bundle)
                                    .buildFiltersJSON(AppServerSpecs.ADVERTISEMENT_PATH)));
            if (arrayList != null && arrayList.size() > 0) {
                Cursor cursor = getContext().getContentResolver().query(AdvertisementContract.CONTENT_URI,
                        null,
                        AdvertUtils.selection(arrayList),
                        AdvertUtils.buildSelectionArgs(arrayList),
                        null);

                if (cursor != null && cursor.getCount() > 0) {
                    ArrayList<Advertisement> advertisements = new ArrayList<>(cursor.getCount());
                    try {
                        if (cursor.moveToFirst()) {
                            do {
                                AdvertisementCursorConverter converter = new AdvertisementCursorConverter();
                                converter.setCursor(cursor);
                                advertisements.add(converter.getObject());
                            } while (cursor.moveToNext());
                        }
                    } finally {
                        cursor.close();
                    }
                    return advertisements;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Long> processResponse(ResponseHolder<ResponseItemHolder<Advertisement>> responseHolder) throws Exception {
        PhotosContentValuesConverter converter = new PhotosContentValuesConverter();
        AdvertFiltersContentValuesConverter filtersConverter = new AdvertFiltersContentValuesConverter();
        ArrayList<Long> advertIds = new ArrayList<>(responseHolder.getData().getItems().size());
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
            advertIds.add(advertisement.getId());
        }
        return advertIds;
    }

    protected <TT> void processItems(ContentResolver cr, Uri uri, List<TT> objects, ContentValuesConverter converter) throws Exception {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>(objects.size());
        for (TT obj : objects) {
            converter.setObjectModel(obj);
            if (converter.isValid()) {
                operations.add(
                        ContentProviderOperation.newUpdate(uri).withSelection(converter.getUpdateWhere(),
                                converter.getUpdateArgs()).withValues(converter.getContentValues()).build());
            }
        }
        cr.applyBatch(uri.getAuthority(), operations);
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

}
