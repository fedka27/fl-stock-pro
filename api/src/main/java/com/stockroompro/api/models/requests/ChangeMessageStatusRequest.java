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

/**
 * Created by user on 14.04.15.
 */
public class ChangeMessageStatusRequest extends BaseRequest implements RequestWithNotifications {
    public ChangeMessageStatusRequest(Context context, Bundle bundle) {
        super(context, bundle);
    }


    @Override
    public ResponseHolder makeRequest() throws Exception {

        return Communicator.getAppServer().setMessageStatus(getBundle().getLong(RequestParams.ParamNames.MESSAGE_ID), new RequestParams()
                .addParameter(RequestParams.ParamNames.STATUS, getBundle().getInt(RequestParams.ParamNames.STATUS))
                .addParameter(RequestParams.ParamNames.TOKEN, getBundle().getString(RequestParams.ParamNames.TOKEN))
                .buildJSONWithPathParams(AppServerSpecs.MESSAGE_STATUS_PATH, AppServerSpecs.MESSAGE_ID, getBundle().getLong(RequestParams.ParamNames.MESSAGE_ID)));
    }

    @Override
    public void processResponse(ResponseHolder responseHolder) throws Exception {

    }

    @Override
    public int getRequestName() {
        return R.string.request_change_message_status;
    }

    @Override
    public int getNotificationPolicy() {
        return NotificationsPolicy.NOTIFY_IF_SERVER_ERROR_RESPONSE | NotificationsPolicy.NOTIFY_IF_ERROR_PROCESSING;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
