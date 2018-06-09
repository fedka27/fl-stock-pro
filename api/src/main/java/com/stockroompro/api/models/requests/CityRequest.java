package com.stockroompro.api.models.requests;

import android.content.Context;
import android.os.Bundle;

import com.artjoker.core.database.CommonDBQueries;
import com.artjoker.core.network.ButchUpdateArrayDatabaseRequest;
import com.artjoker.core.network.NotificationsPolicy;
import com.artjoker.core.network.RequestWithNotifications;
import com.artjoker.core.network.ResponseHolder;
import com.stockroompro.api.AppServerSpecs;
import com.stockroompro.api.Communicator;
import com.stockroompro.api.R;
import com.stockroompro.models.contracts.CityContract;
import com.stockroompro.models.converters.CityContentValuesConverter;
import com.stockroompro.models.location.City;
import com.stockroompro.models.location.Region;

import java.util.ArrayList;

import static com.artjoker.core.database.constants.ExtendedColumns.UPDATED_AT;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.COUNTRY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.DATE;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.REGION_ID;

/**
 * Created by bagach.alexandr on 05.05.15.
 */
public class CityRequest extends ButchUpdateArrayDatabaseRequest<ArrayList<City>, CityContentValuesConverter> implements RequestWithNotifications {

    private static final String TAG = CategoriesRequest.class.getSimpleName();

    public CityRequest(Context context, Bundle bundle) {
        super(context, CityContract.CONTENT_URI, CityContentValuesConverter.class, bundle);
    }

    @Override
    public ResponseHolder<ArrayList<City>> makeRequest() throws Exception {
        return makeRequest(CommonDBQueries.getMaxLongValueByField(getContext(),
                uri, UPDATED_AT, Config.SELECTION, buildSelectionArgs(getBundle().getLong(REGION_ID))));
    }

    @Override
    public ResponseHolder<ArrayList<City>> makeRequest(long date) throws Exception {
        long countryId = getBundle().getLong(COUNTRY_ID);
        long regionId = getBundle().getLong(REGION_ID);
        return Communicator.getAppServer().getCities(countryId, regionId, new RequestParams()
                .addParameter(DATE, date)
                .buildJSONWithPathParams(AppServerSpecs.CITY_PATH,
                        AppServerSpecs.COUNTRY_ID, countryId, AppServerSpecs.REGION_ID, regionId));
    }

    @Override
    public int getRequestName() {
        return R.string.request_get_cities;
    }

    @Override
    public int getNotificationPolicy() {
        return NotificationsPolicy.DO_NOT_NOTIFY_ALL;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    private String[] buildSelectionArgs(long regionId) {
        return new String[]{
                String.valueOf(regionId),
        };
    }

    private interface Config {
        String SELECTION = REGION_ID + "=?";
    }
}
