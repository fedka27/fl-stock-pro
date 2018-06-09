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
import com.stockroompro.models.FilterValues;
import com.stockroompro.models.Filters;
import com.stockroompro.models.contracts.CategoryFiltersContract;
import com.stockroompro.models.contracts.FilterValuesContract;
import com.stockroompro.models.converters.FiltersContentValuesConverter;
import com.stockroompro.models.converters.FiltersValuesContentValuesConverter;

import java.util.ArrayList;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.DATE;

/**
 * Created by bagach.alexandr on 23.04.15.
 */
public class FiltersRequest extends ButchUpdateArrayDatabaseRequest<ArrayList<Filters>, FiltersContentValuesConverter> implements RequestWithNotifications {

    private static final String TAG = FiltersRequest.class.getSimpleName();

    public FiltersRequest(Context context, Bundle bundle) {
        super(context, CategoryFiltersContract.CONTENT_URI, FiltersContentValuesConverter.class, bundle);
    }

    @Override
    public ResponseHolder<ArrayList<Filters>> makeRequest(long date) throws Exception {
        return Communicator.getAppServer().getFilters(new RequestParams().addParameter(DATE, date)
                .buildJSON(AppServerSpecs.FILTERS_ALL));
    }

    @Override
    public void processResponse(ResponseHolder<ArrayList<Filters>> responseHolder) throws Exception {

        FiltersContentValuesConverter filtersContentValuesConverter = new FiltersContentValuesConverter();
        FiltersValuesContentValuesConverter converter = new FiltersValuesContentValuesConverter();
        for (Filters filters : responseHolder.getData()) {
                filtersContentValuesConverter.setCategoryId(filters.getCategoryId());
                processItems(getContext().getContentResolver(), CategoryFiltersContract.CONTENT_URI, filters.getValues(), filtersContentValuesConverter);
            for (FilterValues values : filters.getValues()) {
                converter.setFilterId(values.getId());
                processItems(getContext().getContentResolver(), FilterValuesContract.CONTENT_URI, values.getValue(), converter);
            }
        }
    }

    @Override
    public int getRequestName() {
        return R.string.request_filters;
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
