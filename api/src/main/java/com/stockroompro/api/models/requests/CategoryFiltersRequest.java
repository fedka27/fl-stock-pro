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
import com.stockroompro.models.contracts.CategoryFiltersContract;
import com.stockroompro.models.contracts.FilterValuesContract;
import com.stockroompro.models.converters.FiltersContentValuesConverter;
import com.stockroompro.models.converters.FiltersValuesContentValuesConverter;

import java.util.ArrayList;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.CATEGORY_ID;

/**
 * Created by bagach.alexandr on 23.04.15.
 */
public class CategoryFiltersRequest extends ButchUpdateArrayDatabaseRequest<ArrayList<FilterValues>, FiltersContentValuesConverter> implements RequestWithNotifications {

    private static final String TAG = CategoryFiltersRequest.class.getSimpleName();

    public CategoryFiltersRequest(Context context, Bundle bundle) {
        super(context, CategoryFiltersContract.CONTENT_URI, FiltersContentValuesConverter.class, bundle);
    }

    @Override
    public ResponseHolder<ArrayList<FilterValues>> makeRequest(long date) throws Exception {
        long id = getBundle().getLong(CATEGORY_ID, -1l);
        if (id != -1l) {
            return Communicator.getAppServer().getFilters(id, new RequestParams()
                    .buildJSONWithPathParams(AppServerSpecs.FILTERS_BY_CATEGORY_ID, AppServerSpecs.CATEGORY_ID, id));
        }
        return null;
    }

    @Override
    public void processResponse(ResponseHolder<ArrayList<FilterValues>> responseHolder) throws Exception {

        FiltersContentValuesConverter filtersContentValuesConverter = new FiltersContentValuesConverter();
        FiltersValuesContentValuesConverter converter = new FiltersValuesContentValuesConverter();
        long id = getBundle().getLong(CATEGORY_ID, -1l);
        filtersContentValuesConverter.setCategoryId(id);
        processItems(getContext().getContentResolver(), CategoryFiltersContract.CONTENT_URI, responseHolder.getData(), filtersContentValuesConverter);
        for (FilterValues filters : responseHolder.getData()) {
            converter.setFilterId(filters.getId());
            converter.setCreatedAt(filters.getCreatedAt());
            converter.setUpdatedAt(filters.getUpdatedAt());
            processItems(getContext().getContentResolver(), FilterValuesContract.CONTENT_URI, filters.getValue(), converter);
        }
    }

    @Override
    public int getRequestName() {
        return R.string.request_filters;
    }

    @Override
    public int getNotificationPolicy() {
        return NotificationsPolicy.SHOW_INTERNET_CONNECTION_LOST;
    }

    @Override
    public boolean isValid() {
        return true;
    }

}
