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
import com.stockroompro.api.AppServerSpecs;
import com.stockroompro.api.Communicator;
import com.stockroompro.api.R;
import com.stockroompro.api.models.responses.user.UserRegistration;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_CITY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.COUNTRY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.EMAIL;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.FIRST_NAME;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.LAST_NAME;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.PASSWORD;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.PHONE_NUMBER;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.REGION_ID;


public class RegistrationRequest extends BaseRequest<UserRegistration> implements RequestWithNotifications {

    public RegistrationRequest(Context context, Bundle registrationBundle) {
        super(context, registrationBundle);
    }

    @Override
    public ResponseHolder<UserRegistration> makeRequest() throws Exception {
        return Communicator.getAppServer().registration(new RequestParams()
                .addParameter(FIRST_NAME, getBundle().getString(FIRST_NAME))
                .addParameter(LAST_NAME, getBundle().getString(LAST_NAME))
                .addParameter(PHONE_NUMBER, getBundle().getStringArrayList(PHONE_NUMBER))
                .addParameter(EMAIL, getBundle().getString(EMAIL))
                .addParameter(PASSWORD, getBundle().getString(PASSWORD))
                .addParameter(COUNTRY_ID, getBundle().getLong(COUNTRY_ID, 0))
                .addParameter(REGION_ID, getBundle().getLong(REGION_ID, 0))
                .addParameter(ADVERT_CITY_ID, getBundle().getLong(ADVERT_CITY_ID, 0))
                .buildJSON(AppServerSpecs.REGISTRATION_PATH));
    }

    @Override
    public void processResponse(ResponseHolder responseHolder) throws Exception {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.confirm_registration), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getRequestName() {
        return R.string.request_registration;
    }

    @Override
    public int getNotificationPolicy() {
        return  NotificationsPolicy.NOTIFY_IF_ERROR_PROCESSING | NotificationsPolicy.NOTIFY_SUCCESS | NotificationsPolicy.SHOW_INTERNET_CONNECTION_LOST;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
