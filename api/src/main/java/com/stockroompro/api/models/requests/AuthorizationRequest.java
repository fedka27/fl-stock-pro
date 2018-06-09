package com.stockroompro.api.models.requests;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

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

import java.util.HashSet;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.DEVICE_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.EMAIL;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.PASSWORD;


public class AuthorizationRequest extends BaseRequest<UserAuthorization> implements RequestWithNotifications {


    public AuthorizationRequest(Context context, Bundle authBundle) {
        super(context, authBundle);

    }

    @Override
    public ResponseHolder<UserAuthorization> makeRequest() throws Exception {
        return Communicator.getAppServer().authorization(new RequestParams()
                .addParameter(EMAIL, getBundle().getString(EMAIL))
                .addParameter(PASSWORD, getBundle().getString(PASSWORD))
                .addParameter(DEVICE_ID, SystemHelper.getInstance().getUniqueDeviceId())
                .buildJSON(AppServerSpecs.AUTHORIZATION_PATH));

    }

    @Override
    public void processResponse(ResponseHolder<UserAuthorization> responseHolder) throws Exception {
        switch (responseHolder.getStatusCode()) {
            case ResponseHolder.StatusCode.SUCCESSFULL:
                PersonalData personalData = PersonalData.getInstance(getContext());
                personalData.setUserEmail(responseHolder.getData().getEmail());
                personalData.setUserId(responseHolder.getData().getId());
                personalData.setUserPicture(responseHolder.getData().getPic());
                personalData.setUserToken(responseHolder.getData().getToken());
                personalData.setUserServiceId(responseHolder.getData().getServiceId());
                personalData.setUserPhones(new HashSet<String>(responseHolder.getData().getPhones()));
                personalData.setUserCountryId(responseHolder.getData().getCountryId());
                personalData.setUserCountryName(responseHolder.getData().getCountryName());
                personalData.setUserRegionId(responseHolder.getData().getRegionId());
                personalData.setUserRegionName(responseHolder.getData().getRegionName());
                personalData.setUserCityId(responseHolder.getData().getCityId());
                personalData.setUserCityName(responseHolder.getData().getCityName());
                break;
            case ResponseHolder.StatusCode.INACTIVE_USER:
                showInactiveUserToast(responseHolder.getReason());
                break;

        }


    }

    private void showInactiveUserToast(final String reason) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), reason, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getRequestName() {
        return R.string.request_authorization;
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
