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
import com.stockroompro.api.models.responses.user.UserData;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.EMAIL;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.FIRST_NAME;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.LAST_NAME;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.OLD_PASSWORD;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.PASSWORD;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.UID;

/**
 * Created by user on 09.04.15.
 */
public class ChangePasswordRequest  extends BaseRequest<UserData> implements RequestWithNotifications {

    public ChangePasswordRequest(Context context,Bundle bundle) {
        super(context, bundle);
    }

    @Override
    public ResponseHolder<UserData> makeRequest() throws Exception {
        return Communicator.getAppServer().editUser(getBundle().getLong(UID), new RequestParams()
                .addParameter(TOKEN, getBundle().getString(TOKEN))
                .addParameter(FIRST_NAME, getBundle().getString(FIRST_NAME))
                .addParameter(EMAIL, getBundle().getString(EMAIL))
                .addParameter(LAST_NAME, getBundle().getString(LAST_NAME))
                .addParameter(PASSWORD, getBundle().getString(PASSWORD))
                .addParameter(OLD_PASSWORD, getBundle().getString(OLD_PASSWORD))
                .buildJSONWithPathParams(AppServerSpecs.USER_PATH, AppServerSpecs.USER_ID, getBundle().getLong(UID)));
    }

    @Override
    public void processResponse(ResponseHolder<UserData> responseHolder) throws Exception {

    }

    @Override
    public int getRequestName() {
        return R.string.request_change_password;
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
