package com.stockroompro.api.models.requests;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.artjoker.core.network.ButchUpdateArrayDatabaseRequest;
import com.artjoker.core.network.NotificationsPolicy;
import com.artjoker.core.network.RequestWithNotifications;
import com.artjoker.core.network.ResponseHolder;
import com.artjoker.core.network.ResponseItemHolder;
import com.artjoker.core.network.UpdateDatabaseRequest;
import com.stockroompro.api.AppServerSpecs;
import com.stockroompro.api.Communicator;
import com.stockroompro.api.R;
import com.stockroompro.models.AdvertFilters;
import com.stockroompro.models.Advertisement;
import com.stockroompro.models.Photo;
import com.stockroompro.models.contracts.AdvertFiltersContract;
import com.stockroompro.models.contracts.PhotosContract;
import com.stockroompro.models.converters.AdvertFiltersContentValuesConverter;
import com.stockroompro.models.converters.AdvertFiltersCursorConverter;
import com.stockroompro.models.converters.AdvertisementContentValuesConverter;
import com.stockroompro.models.converters.PhotosContentValuesConverter;

import java.util.ArrayList;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.AD_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;

/**
 * Created by user on 27.04.15.
 */
public class AdvertisementDetailsRequest extends UpdateDatabaseRequest<Advertisement,AdvertisementContentValuesConverter> implements RequestWithNotifications {
    public AdvertisementDetailsRequest(Context context, Uri uri, Class<AdvertisementContentValuesConverter> advertisementContentValuesConverterClass, Bundle bundle) {
        super(context, uri, advertisementContentValuesConverterClass, bundle);
    }

    @Override
    public ResponseHolder<Advertisement> makeRequest(long date) throws Exception {
        if (getBundle().containsKey(TOKEN)) {
            return Communicator.getAppServer().getAdvertisementDetails(getBundle().getLong(AD_ID),
                    new RequestParams()
                            .addParameter(TOKEN, getBundle().getString(TOKEN))
                            .buildJSONWithPathParams(AppServerSpecs.ADVERTISEMENT_DETAILS_PATH,
                                    AppServerSpecs.ADVERTISEMENT_ID, getBundle().getLong(AD_ID)));
        } else {
            return Communicator.getAppServer().getAdvertisementDetails(getBundle().getLong(AD_ID),
                    new RequestParams().buildJSONWithPathParams(AppServerSpecs.ADVERTISEMENT_DETAILS_PATH,
                            AppServerSpecs.ADVERTISEMENT_ID, getBundle().getLong(AD_ID)));
        }
    }

    @Override
    public void processResponse(ResponseHolder<Advertisement> responseHolder) throws Exception {
        super.processResponse(responseHolder);
        PhotosContentValuesConverter converter = new PhotosContentValuesConverter();
        AdvertFiltersContentValuesConverter advertFiltersContentValuesConverter = new AdvertFiltersContentValuesConverter();
        Advertisement advertisement = responseHolder.getData();
        getContext().getContentResolver().delete(PhotosContract.CONTENT_URI,
                PhotosContract.ADVERTISEMENT_ID + " = ?", new String[]{String.valueOf(advertisement.getId())});
        converter.setAdvertisementId(advertisement.getId());
        converter.setCreatedAt(advertisement.getCreatedAt());
        converter.setUpdatedAt(advertisement.getUpdatedAt());
        for (String photo : advertisement.getPhotos()) {
            converter.setObjectModel(photo);
            processItem(getContext().getContentResolver(), PhotosContract.CONTENT_URI, converter);
        }

        if (advertisement.getFilters() != null) {
            for (long filterValueId : advertisement.getFilters()) {
                AdvertFilters af = new AdvertFilters();
                af.setAdvertId(advertisement.getId());
                af.setFilterValueId(filterValueId);
                advertFiltersContentValuesConverter.setObjectModel(af);
                processItem(getContext().getContentResolver(), AdvertFiltersContract.CONTENT_URI, advertFiltersContentValuesConverter);
            }

        }
    }

    @Override
    public int getRequestName() {
        return R.string.request_advert_details;
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
