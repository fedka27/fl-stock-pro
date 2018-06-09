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
import com.stockroompro.models.Category;
import com.stockroompro.models.contracts.CategoryContract;
import com.stockroompro.models.converters.CategoryContentValuesConverter;

import java.util.ArrayList;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.DATE;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.CATEGORY_ID;

/**
 * Created by alexsergienko on 24.03.15.
 */
public class CategoriesRequest extends ButchUpdateArrayDatabaseRequest<ArrayList<Category>, CategoryContentValuesConverter> implements RequestWithNotifications {

    private static final String TAG = CategoriesRequest.class.getSimpleName();

    public CategoriesRequest(Context context, Bundle bundle) {
        super(context, CategoryContract.CONTENT_URI, CategoryContentValuesConverter.class, bundle);
    }

    @Override
    public ResponseHolder<ArrayList<Category>> makeRequest(long date) throws Exception {
        return Communicator.getAppServer().getSubCategories(getBundle().getLong(CATEGORY_ID), new RequestParams().addParameter(DATE, date)
                .buildJSONWithPathParams(AppServerSpecs.SUB_CATEGORIES_PATH, AppServerSpecs.CATEGORY_ID, getBundle().getLong(CATEGORY_ID)));
    }

    @Override
    public int getRequestName() {
        return R.string.request_categories;
    }

    @Override
    public int getNotificationPolicy() {
        return  NotificationsPolicy.SHOW_INTERNET_CONNECTION_LOST;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
