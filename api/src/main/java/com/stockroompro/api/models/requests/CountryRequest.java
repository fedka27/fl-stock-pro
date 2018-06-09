package com.stockroompro.api.models.requests;

import android.content.Context;
import android.os.Bundle;

import com.artjoker.core.network.ButchUpdateArrayDatabaseRequest;
import com.artjoker.core.network.NotificationsPolicy;
import com.artjoker.core.network.RequestWithNotifications;
import com.artjoker.core.network.ResponseHolder;
import com.stockroompro.api.AppServerSpecs;
import com.stockroompro.api.Communicator;
import com.stockroompro.api.R;
import com.stockroompro.models.contracts.CountryContract;
import com.stockroompro.models.converters.CountryContentValuesConverter;
import com.stockroompro.models.location.Country;

import java.util.ArrayList;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.DATE;

/**
 * Created by bagach.alexandr on 05.05.15.
 */
public class CountryRequest extends ButchUpdateArrayDatabaseRequest<ArrayList<Country>, CountryContentValuesConverter> implements RequestWithNotifications {

    private static final String TAG = CategoriesRequest.class.getSimpleName();

    public CountryRequest(Context context, Bundle bundle) {
        super(context, CountryContract.CONTENT_URI, CountryContentValuesConverter.class, bundle);
    }

    @Override
    public ResponseHolder<ArrayList<Country>> makeRequest(long date) throws Exception {
        return Communicator.getAppServer().getCountries(new RequestParams()
                .addParameter(DATE, date)
                .buildJSON(AppServerSpecs.COUNTRY_PATH));
    }

    @Override
    public int getRequestName() {
        return R.string.request_get_countries;
    }

    @Override
    public int getNotificationPolicy() {
        return NotificationsPolicy.DO_NOT_NOTIFY_ALL;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
