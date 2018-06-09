package com.stockroompro.api.models.requests;

import android.content.Context;
import android.os.Bundle;

import com.artjoker.core.network.BaseRequest;
import com.artjoker.core.network.NotificationsPolicy;
import com.artjoker.core.network.RequestWithNotifications;
import com.artjoker.core.network.ResponseHolder;
import com.artjoker.tool.core.SystemHelper;
import com.stockroompro.api.AppServerSpecs;
import com.stockroompro.api.Communicator;
import com.stockroompro.api.R;
import com.stockroompro.api.models.responses.user.UserAuthorization;
import com.stockroompro.models.PersonalData;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.DEVICE_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.SERVICE_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.SERVICE_TOKEN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.UID;


public class SocialNetworkAuthRequest extends BaseRequest<UserAuthorization> implements RequestWithNotifications {

    public SocialNetworkAuthRequest(Context context,Bundle bundle) {
        super(context,bundle);
    }

    @Override
    public ResponseHolder<UserAuthorization> makeRequest() throws Exception {
        return Communicator.getAppServer().authorization(new RequestParams()
                .addParameter(UID, getBundle().getLong(UID))
                .addParameter(SERVICE_TOKEN, getBundle().getString(SERVICE_TOKEN))
                .addParameter(SERVICE_ID, getBundle().getInt(SERVICE_ID))
                .addParameter(DEVICE_ID, SystemHelper.getInstance().getUniqueDeviceId())
                .buildJSON(AppServerSpecs.AUTHORIZATION_PATH));
    }

    @Override
    public void processResponse(ResponseHolder<UserAuthorization> responseHolder) throws Exception {
        PersonalData.getInstance(getContext()).setUserEmail(responseHolder.getData().getEmail());
        PersonalData.getInstance(getContext()).setUserId(responseHolder.getData().getId());
        PersonalData.getInstance(getContext()).setUserPicture(responseHolder.getData().getPic());
        PersonalData.getInstance(getContext()).setUserServiceToken(responseHolder.getData().getToken());
        PersonalData.getInstance(getContext()).setUserServiceId(responseHolder.getData().getServiceId());
    }

    @Override
    public int getRequestName() {
        return R.string.request_social_authorization;
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
