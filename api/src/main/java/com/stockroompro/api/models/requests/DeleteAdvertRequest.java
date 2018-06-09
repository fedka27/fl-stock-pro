package com.stockroompro.api.models.requests;

import android.content.Context;
import android.os.Bundle;

import com.artjoker.core.network.BaseRequest;
import com.artjoker.core.network.NotificationsPolicy;
import com.artjoker.core.network.RequestWithNotifications;
import com.artjoker.core.network.ResponseHolder;
import com.stockroompro.api.AppServerSpecs;
import com.stockroompro.api.Communicator;
import com.stockroompro.api.R;
import com.stockroompro.models.columns.AdvertisementColumns;
import com.stockroompro.models.contracts.AdvertisementContract;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.AD_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.UID;

/**
 * Created by user on 27.05.15.
 */
public class DeleteAdvertRequest extends BaseRequest implements RequestWithNotifications {
    public DeleteAdvertRequest(Context context, Bundle bundle) {
        super(context, bundle);
    }

    @Override
    public ResponseHolder makeRequest() throws Exception {
        return Communicator.getAppServer().deleteAdvert(getBundle().getLong(UID), getBundle().getLong(AD_ID), new RequestParams()
                .addParameter(TOKEN, getBundle().getString(TOKEN))
                .buildJSONWithPathParams(AppServerSpecs.DELETE_ADVERT_PATH,
                        AppServerSpecs.USER_ID, getBundle().getLong(UID),
                        AppServerSpecs.ADVERTISEMENT_ID, getBundle().getLong(AD_ID)
                ));
    }

    @Override
    public void processResponse(ResponseHolder responseHolder) throws Exception {
        if (responseHolder.getStatusCode() == 0) {
            getContext().getContentResolver().delete(AdvertisementContract.CONTENT_URI, AdvertisementColumns.ID + " = ?",
                        new String[] {String.valueOf(getBundle().getLong(AD_ID))});
        }
    }

    @Override
    public int getRequestName() {
        return R.string.request_delete_advert;
    }

    @Override
    public int getNotificationPolicy() {
        return NotificationsPolicy.NOTIFY_IF_SERVER_ERROR_RESPONSE | NotificationsPolicy.NOTIFY_IF_ERROR_PROCESSING| NotificationsPolicy.SHOW_INTERNET_CONNECTION_LOST;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
