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
import com.stockroompro.api.models.responses.user.UserRegistration;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.SERVICE;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.UID;

/**
 * Created by user on 02.04.15.
 */
public class SocialNetworkRegRequest extends BaseRequest<UserRegistration> implements RequestWithNotifications {

    public SocialNetworkRegRequest(Context context,Bundle bundle) {
        super(context,bundle);

    }

    @Override
    public ResponseHolder<UserRegistration> makeRequest() throws Exception {

        return Communicator.getAppServer().registration(new RequestParams()
                .addParameter(UID, getBundle().getLong(UID))
                .addParameter(TOKEN, getBundle().getString(TOKEN))
                .addParameter(SERVICE, getBundle().getInt(SERVICE))
                .buildJSON(AppServerSpecs.REGISTRATION_PATH));
    }

    @Override
    public void processResponse(ResponseHolder<UserRegistration> responseHolder) throws Exception {

    }

    @Override
    public int getRequestName() {
        return R.string.request_social_registration;
    }

    @Override
    public int getNotificationPolicy() {
        return NotificationsPolicy.NOTIFY_IF_SERVER_ERROR_RESPONSE | NotificationsPolicy.NOTIFY_IF_ERROR_PROCESSING | NotificationsPolicy.NOTIFY_SUCCESS| NotificationsPolicy.SHOW_INTERNET_CONNECTION_LOST;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
