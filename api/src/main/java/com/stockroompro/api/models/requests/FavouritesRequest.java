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
import com.stockroompro.models.PersonalData;
import com.stockroompro.models.contracts.AdvertisementContract;
import com.stockroompro.models.contracts.PhotosContract;
import com.stockroompro.models.converters.AdvertisementContentValuesConverter;
import com.stockroompro.models.converters.PhotosContentValuesConverter;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.CATEGORY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.LIMIT;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.OFFSET;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.UID;

/**
 * Created by bagach.alexandr on 03.04.15.
 */
public class FavouritesRequest extends ButchUpdateArrayDatabaseRequest<ResponseItemHolder<Advertisement>, AdvertisementContentValuesConverter> implements RequestWithNotifications {

    public FavouritesRequest(Context context, Bundle args) {
        super(context, AdvertisementContract.CONTENT_URI, AdvertisementContentValuesConverter.class, args);
    }

    @Override
    public ResponseHolder<ResponseItemHolder<Advertisement>> makeRequest(long date) throws Exception {
        return Communicator.getAppServer().getFavourites(getBundle().getLong(UID), new RequestParams()
                .addParameter(TOKEN, getBundle().getString(TOKEN))
                .buildJSONWithPathParams(AppServerSpecs.FAVOURITES_PATH,
                        AppServerSpecs.USER_ID, getBundle().getLong(UID)));
    }

    @Override
    public void processResponse(ResponseHolder<ResponseItemHolder<Advertisement>> responseHolder) throws Exception {

        PhotosContentValuesConverter converter = new PhotosContentValuesConverter();
        for (Advertisement advertisement : responseHolder.getData().getItems()) {
            advertisement.setIsFavourite(1);
            getContext().getContentResolver().delete(PhotosContract.CONTENT_URI,
                    PhotosContract.ADVERTISEMENT_ID + " = ?", new String[]{String.valueOf(advertisement.getId())});
            converter.setAdvertisementId(advertisement.getId());
            processItems(getContext().getContentResolver(), PhotosContract.CONTENT_URI, advertisement.getPhotos(), converter);
        }
        super.processResponse(responseHolder);
    }

    @Override
    public int getNotificationPolicy() {
        return NotificationsPolicy.NOTIFY_IF_ERROR_PROCESSING| NotificationsPolicy.SHOW_INTERNET_CONNECTION_LOST;
    }


    @Override
    public int getRequestName() {
        return R.string.favourites;
    }

    @Override
    public boolean isValid() {
        return PersonalData.getInstance(getContext()).getToken() != null;
    }
}
