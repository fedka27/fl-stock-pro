package com.stockroompro.api.models.requests;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.artjoker.core.network.BaseRequest;
import com.artjoker.core.network.ButchUpdateArrayDatabaseRequest;
import com.artjoker.core.network.NotificationsPolicy;
import com.artjoker.core.network.RequestWithNotifications;
import com.artjoker.core.network.ResponseHolder;
import com.stockroompro.api.AppServerSpecs;
import com.stockroompro.api.BuildConfig;
import com.stockroompro.api.Communicator;
import com.stockroompro.api.R;
import com.stockroompro.models.AdvertPhoto;
import com.stockroompro.models.contracts.PhotosContract;
import com.stockroompro.models.converters.PhotosContentValuesConverter;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.AD_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.DELETED_PHOTOS;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;

/**
 * Created by bagach.alexandr on 29.04.15.
 */
public class AddAdvertisementPhotos extends ButchUpdateArrayDatabaseRequest<AdvertPhoto, PhotosContentValuesConverter> implements RequestWithNotifications {

    private HashMap<String, TypedFile> photos;

    public AddAdvertisementPhotos(Context context, Bundle bundle, HashMap<String, TypedFile> photos) {
        super(context, PhotosContract.CONTENT_URI, PhotosContentValuesConverter.class, bundle);
        this.photos = photos;
    }

    @Override
    public ResponseHolder<AdvertPhoto> makeRequest(long date) throws Exception {
        TypedString token = new TypedString(getBundle().getString(TOKEN));
        TypedString appId = new TypedString(BuildConfig.SERVER_APP_ID);
        ArrayList<String> deletedPhotos = getBundle().getStringArrayList(DELETED_PHOTOS);
        long time = RequestParams.REQUEST_TIME;
        TypedString hash = new TypedString(new RequestParams()
                .addParameter(TOKEN, getBundle().getString(TOKEN))
                .addParameter(DELETED_PHOTOS, deletedPhotos)
                .getHashOfJSONWithPathParams(AppServerSpecs.ADD_ADVERTISEMENT_PHOTOS, AppServerSpecs.ADVERTISEMENT_ID, getBundle().getLong(AD_ID)));
        return Communicator.getAppServer().sendPhotos(getBundle().getLong(AD_ID), photos, appId, time, token, deletedPhotos, hash);
    }

    @Override
    public int getRequestName() {
        return R.string.request_add_photo_to_advert;
    }

    @Override
    public int getNotificationPolicy() {
        return NotificationsPolicy.NOTIFY_IF_SERVER_ERROR_RESPONSE | NotificationsPolicy.NOTIFY_IF_ERROR_PROCESSING | NotificationsPolicy.NOTIFY_SUCCESS;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void processResponse(ResponseHolder<AdvertPhoto> responseHolder) throws Exception {
        Log.e("ResponseHolder", "getStatusCode" + responseHolder.getStatusCode());
    }

    @Override
    public BaseRequest<AdvertPhoto> setUiCallback(UIResponseCallback uiCallback) {
        return super.setUiCallback(uiCallback);
    }
}
