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
import com.stockroompro.models.Category;
import com.stockroompro.models.contracts.RegionContract;
import com.stockroompro.models.converters.RegionContentValuesConverter;
import com.stockroompro.models.location.Region;

import java.util.ArrayList;

import static com.artjoker.core.database.constants.ExtendedColumns.PARENT_ID;
import static com.artjoker.core.database.constants.ExtendedColumns.UPDATED_AT;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.CATEGORY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.COUNTRY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.DATE;

/**
 * Created by bagach.alexandr on 05.05.15.
 */
public class RegionRequest extends ButchUpdateArrayDatabaseRequest<ArrayList<Region>, RegionContentValuesConverter> implements RequestWithNotifications {

    private static final String TAG = CategoriesRequest.class.getSimpleName();

    public RegionRequest(Context context, Bundle bundle) {
        super(context, RegionContract.CONTENT_URI, RegionContentValuesConverter.class, bundle);
    }

    @Override
    public ResponseHolder<ArrayList<Region>> makeRequest() throws Exception {
        return makeRequest(CommonDBQueries.getMaxLongValueByField(getContext(),
                uri, UPDATED_AT, Config.SELECTION, buildSelectionArgs(getBundle().getLong(COUNTRY_ID))));
    }

    @Override
    public ResponseHolder<ArrayList<Region>> makeRequest(long date) throws Exception {
        return Communicator.getAppServer().getRegions(getBundle().getLong(COUNTRY_ID), new RequestParams()
                .addParameter(DATE, date)
                .buildJSONWithPathParams(AppServerSpecs.REGION_PATH, AppServerSpecs.COUNTRY_ID, getBundle().getLong(COUNTRY_ID)));
    }

    @Override
    public int getRequestName() {
        return R.string.request_get_regions;
    }

    @Override
    public int getNotificationPolicy() {
        return NotificationsPolicy.DO_NOT_NOTIFY_ALL;
    }

    @Override
    public boolean isValid() {
        return true;
    }


    private String[] buildSelectionArgs(long id) {
        return new String[]{
                String.valueOf(id),
        };
    }

    private interface Config {
        String SELECTION = COUNTRY_ID + "=?";
    }
}
