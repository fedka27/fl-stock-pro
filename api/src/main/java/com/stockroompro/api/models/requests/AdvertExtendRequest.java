package com.stockroompro.api.models.requests;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;

import com.artjoker.core.network.ButchUpdateArrayDatabaseRequest;
import com.artjoker.core.network.NotificationsPolicy;
import com.artjoker.core.network.RequestWithNotifications;
import com.artjoker.core.network.ResponseHolder;
import com.stockroompro.api.AppServerSpecs;
import com.stockroompro.api.Communicator;
import com.stockroompro.api.R;
import com.stockroompro.models.Advertisement;
import com.stockroompro.models.ExpiryDate;
import com.stockroompro.models.columns.AdvertisementColumns;
import com.stockroompro.models.contracts.AdvertisementContract;
import com.stockroompro.models.converters.AdvertisementContentValuesConverter;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ACTION;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.AD_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.USER_ID;

/**
 * Created by bagach.alexandr on 02.06.15.
 */
public class AdvertExtendRequest extends ButchUpdateArrayDatabaseRequest<ExpiryDate, AdvertisementContentValuesConverter> implements RequestWithNotifications {

    public AdvertExtendRequest(Context context, Bundle bundle) {
        super(context, AdvertisementContract.CONTENT_URI, AdvertisementContentValuesConverter.class, bundle);
    }

    @Override
    public ResponseHolder<ExpiryDate> makeRequest(long date) throws Exception {
        return Communicator.getAppServer().extendAdvert(getBundle().getLong(USER_ID), getBundle().getLong(AD_ID),
                new RequestParams()
                        .addParameter(ACTION, getBundle().getString(ACTION))
                        .addParameter(TOKEN, getBundle().getString(TOKEN))
                        .buildJSONWithPathParams(AppServerSpecs.EXTEND_ADVERT_PATH,
                                AppServerSpecs.USER_ID, getBundle().getLong(USER_ID),
                                AppServerSpecs.ADVERTISEMENT_ID, getBundle().getLong(AD_ID)));
    }

    @Override
    public void processResponse(ResponseHolder<ExpiryDate> responseHolder) throws Exception {
        ContentValues cv = new ContentValues();
        cv.put(AdvertisementColumns.EXPIRY_DATE, responseHolder.getData().getExpiryDate());
        getContext().getContentResolver().update(AdvertisementContract.CONTENT_URI, cv, AdvertisementColumns.ID + " = ?",
                new String[] {String.valueOf(getBundle().getLong(AD_ID))});
    }

    @Override
    public int getRequestName() {
        return R.string.request_extend_advert;
    }

    @Override
    public int getNotificationPolicy() {
        return NotificationsPolicy.NOTIFY_IF_SERVER_ERROR_RESPONSE | NotificationsPolicy.NOTIFY_IF_ERROR_PROCESSING | NotificationsPolicy.NOTIFY_SUCCESS | NotificationsPolicy.SHOW_INTERNET_CONNECTION_LOST;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}