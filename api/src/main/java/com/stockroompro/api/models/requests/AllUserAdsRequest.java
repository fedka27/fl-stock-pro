package com.stockroompro.api.models.requests;

import android.content.Context;
import android.os.Bundle;

import com.artjoker.core.network.ButchUpdateArrayDatabaseRequest;
import com.artjoker.core.network.NotificationsPolicy;
import com.artjoker.core.network.RequestWithNotifications;
import com.artjoker.core.network.ResponseHolder;
import com.artjoker.core.network.ResponseItemHolder;
import com.stockroompro.api.AppServerSpecs;
import com.stockroompro.api.Communicator;
import com.stockroompro.api.R;
import com.stockroompro.models.Advertisement;
import com.stockroompro.models.contracts.AdvertisementContract;
import com.stockroompro.models.contracts.PhotosContract;
import com.stockroompro.models.converters.AdvertisementContentValuesConverter;
import com.stockroompro.models.converters.PhotosContentValuesConverter;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.LIMIT;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.OFFSET;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.UID;

/**
 * Created by user on 23.04.15.
 */
public class AllUserAdsRequest extends ButchUpdateArrayDatabaseRequest<ResponseItemHolder<Advertisement>, AdvertisementContentValuesConverter> implements RequestWithNotifications {
    public AllUserAdsRequest(Context context, Bundle bundle) {
        super(context, AdvertisementContract.CONTENT_URI, AdvertisementContentValuesConverter.class, bundle);
    }

    @Override
    public ResponseHolder<ResponseItemHolder<Advertisement>> makeRequest(long date) throws Exception {
        return Communicator.getAppServer().getAdvertsByUserId(getBundle().getLong(UID), new RequestParams()
                .addParameter(TOKEN, getBundle().getString(TOKEN))
                .addParameter(LIMIT, getBundle().getInt(LIMIT))
                .addParameter(OFFSET, getBundle().getInt(OFFSET))
                .buildJSONWithPathParams(AppServerSpecs.USER_ADVERTISEMENT_PATH, AppServerSpecs.USER_ID, getBundle().getLong(UID)));
    }

    @Override
    public void processResponse(ResponseHolder<ResponseItemHolder<Advertisement>> responseHolder) throws Exception {
        super.processResponse(responseHolder);
        PhotosContentValuesConverter converter = new PhotosContentValuesConverter();
        for (Advertisement advertisement : responseHolder.getData().getItems()) {
            getContext().getContentResolver().delete(PhotosContract.CONTENT_URI,
                    PhotosContract.ADVERTISEMENT_ID + " = ?", new String[]{String.valueOf(advertisement.getId())});
            converter.setAdvertisementId(advertisement.getId());
            processItems(getContext().getContentResolver(), PhotosContract.CONTENT_URI, advertisement.getPhotos(), converter);
        }
    }

    @Override
    public int getRequestName() {
        return R.string.request_all_users_ads;
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
