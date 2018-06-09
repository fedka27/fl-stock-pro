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
import com.stockroompro.models.PersonalData;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.UID;

/**
 * Created by user on 20.04.15.
 */
public class DeleteProfilePhotoRequest extends BaseRequest implements RequestWithNotifications {
    public DeleteProfilePhotoRequest(Context context, Bundle bundle) {
        super(context, bundle);
    }

    @Override
    public ResponseHolder makeRequest() throws Exception {
        return Communicator.getAppServer().deleteProfilePhoto(getBundle().getLong(UID), new RequestParams()
                .addParameter(TOKEN, getBundle().getString(TOKEN))
                .buildJSONWithPathParams(AppServerSpecs.EDIT_PROFILE_PHOTO_PATH, AppServerSpecs.USER_ID, getBundle().getLong(UID)));
    }

    @Override
    public void processResponse(ResponseHolder responseHolder) throws Exception {
        switch (responseHolder.getStatusCode()) {
            case ResponseHolder.StatusCode.SUCCESSFULL:
                PersonalData.getInstance(getContext()).setUserPicture("");
                break;


        }

    }

    @Override
    public int getRequestName() {
        return R.string.request_delete_profile_photo;
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
