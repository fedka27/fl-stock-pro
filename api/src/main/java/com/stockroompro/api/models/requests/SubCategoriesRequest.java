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
import com.stockroompro.models.contracts.CategoryContract;
import com.stockroompro.models.converters.SubCategoryContentValuesConverter;

import java.util.ArrayList;

import static com.artjoker.core.database.constants.ExtendedColumns.PARENT_ID;
import static com.artjoker.core.database.constants.ExtendedColumns.UPDATED_AT;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.CATEGORY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.DATE;

/**
 * Created by bagach.alexandr on 25.03.15.
 */
public class SubCategoriesRequest extends ButchUpdateArrayDatabaseRequest<ArrayList<Category>, SubCategoryContentValuesConverter> implements RequestWithNotifications {

    private static final String TAG = CategoriesRequest.class.getSimpleName();

    public SubCategoriesRequest(Context context, Bundle bundle) {
        super(context, CategoryContract.CONTENT_URI, SubCategoryContentValuesConverter.class, bundle);
    }

    @Override
    public ResponseHolder<ArrayList<Category>> makeRequest() throws Exception {
        return makeRequest(CommonDBQueries.getMaxLongValueByField(getContext(),
                uri, UPDATED_AT, Config.SELECTION, buildSelectionArgs(getBundle().getLong(CATEGORY_ID))));
    }

    @Override
    public ResponseHolder<ArrayList<Category>> makeRequest(long date) throws Exception {
        return Communicator.getAppServer().getSubCategories(getBundle().getLong(CATEGORY_ID), new RequestParams().addParameter(DATE, date)
                .buildJSONWithPathParams(AppServerSpecs.SUB_CATEGORIES_PATH, AppServerSpecs.CATEGORY_ID, getBundle().getLong(CATEGORY_ID)));
    }

    @Override
    public int getRequestName() {
        return R.string.request_sub_categories;
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
        String SELECTION = PARENT_ID + "=?";
    }
}
