package com.stockroompro.api.models.requests;

import android.content.Context;
import android.os.Bundle;

import com.artjoker.core.network.BaseRequest;
import com.artjoker.core.network.NotificationsPolicy;
import com.artjoker.core.network.RequestWithNotifications;
import com.artjoker.core.network.ResponseHolder;
import com.stockroompro.api.AppServerSpecs;
import com.stockroompro.api.BuildConfig;
import com.stockroompro.api.Communicator;
import com.stockroompro.api.R;

import java.io.File;

import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.FILE_PATH;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.UID;

/**
 * Created by user on 20.04.15.
 */
public class EditProfilePhotoRequest extends BaseRequest implements RequestWithNotifications {
    public EditProfilePhotoRequest(Context context, Bundle bundle) {
        super(context, bundle);
    }

    @Override
    public ResponseHolder makeRequest() throws Exception {
        TypedString token = new TypedString(getBundle().getString(TOKEN));
        TypedString appId = new TypedString(BuildConfig.SERVER_APP_ID);
        long time = RequestParams.REQUEST_TIME;
        TypedFile photo = new TypedFile("image/jpeg", new File(getBundle().getString(FILE_PATH)));
        TypedString hash = new TypedString(new RequestParams()
                .addParameter(TOKEN, getBundle().getString(TOKEN))
                .getHashOfJSONWithPathParams(AppServerSpecs.EDIT_PROFILE_PHOTO_PATH, AppServerSpecs.USER_ID, getBundle().getLong(UID)));
        return Communicator.getAppServer().editProfilePhoto(getBundle().getLong(UID), photo, appId, time, token, hash);

    }

    @Override
    public void processResponse(ResponseHolder responseHolder) throws Exception {

    }

    @Override
    public int getRequestName() {
        return R.string.request_edit_profile_photo;
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
