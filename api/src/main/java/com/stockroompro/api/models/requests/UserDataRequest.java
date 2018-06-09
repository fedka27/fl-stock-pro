package com.stockroompro.api.models.requests;

import android.content.Context;
import android.os.Bundle;

import com.artjoker.core.network.NotificationsPolicy;
import com.artjoker.core.network.RequestWithNotifications;
import com.artjoker.core.network.ResponseHolder;
import com.artjoker.core.network.UpdateDatabaseRequest;
import com.stockroompro.api.AppServerSpecs;
import com.stockroompro.api.Communicator;
import com.stockroompro.api.R;
import com.stockroompro.models.UserData;
import com.stockroompro.models.contracts.UserDataContract;
import com.stockroompro.models.converters.UserDataContentValuesConverter;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.USER_ID;

/**
 * Created by bagach.alexandr on 27.04.15.
 */
public class UserDataRequest extends UpdateDatabaseRequest<UserData, UserDataContentValuesConverter> implements RequestWithNotifications {

    public UserDataRequest(Context context, Bundle bundle) {
        super(context, UserDataContract.CONTENT_URI, UserDataContentValuesConverter.class, bundle);
    }

    @Override
    public ResponseHolder<UserData> makeRequest(long date) throws Exception {
        return Communicator.getAppServer().getUserData(getBundle().getLong(USER_ID),
                new RequestParams().buildJSONWithPathParams(AppServerSpecs.USER_PATH, AppServerSpecs.USER_ID, getBundle().getLong(USER_ID)));
    }

    @Override
    public int getRequestName() {
        return R.string.request_user_data;
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
